import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IDepartment } from 'app/entities/department/department.model';
import { ISearchCriteria, SearchCriteria } from 'app/entities/util/criteria.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { MeetingService } from '../service/meeting.service';
import { IMeetingDelivery } from '../meeting.model';
import { ResponseCode } from 'app/entities/util/reply-message.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import * as dayjs from 'dayjs';
import { MEETING_SENT_KEY } from 'app/config/input.constants';

@Component({
  selector: 'jhi-meeting-received',
  templateUrl: './meeting-received.component.html',
  styleUrls: ['./meeting-received.component.scss'],
})
export class MeetingReceivedComponent implements OnInit, OnDestroy {
  isShowingFilters = true;
  isShowingResult = false;
  isShowingAlert = false;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  departmentsList?: IDepartment[];
  meetingDeliveryList?: IMeetingDelivery[];
  _departmentName: string | undefined = '';

  searchForm = this.fb.group({
    fromdate: [],
    todate: [],
    subject: [],
    departmentID: [0],
    status: [2],
    docno: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected translateService: TranslateService,
    protected loadSetupService: LoadSetupService,
    protected meetingService: MeetingService,
    protected userAuthorityService: UserAuthorityService
  ) {}

  ngOnInit(): void {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];

        const searchedCriteria = this.meetingService.getSearchCriteria(MEETING_SENT_KEY);
        if (searchedCriteria) {
          this.updateCriteriaData(searchedCriteria);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnDestroy(): void {
    this.meetingService.clearPreviousState();
  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
  }

  goToViewDetails(id?: number): void {
    this.meetingService.storeSearchCriteria(MEETING_SENT_KEY, this.createCriteriaData());
    this.router.navigate(['/meeting', id, 'view']);
  }

  searchMeetingInvitation(): void {
    if (this.searchForm.invalid) {
      this.isShowingResult = true;
      this.isShowingAlert = true;
    } else {
      this.loadPage(1);
    }
  }

  clearForm(): void {
    this.searchForm.get(['fromdate'])?.patchValue('');
    this.searchForm.get(['todate'])?.patchValue('');
    this.searchForm.get(['departmentID'])?.patchValue(0);
    this.searchForm.get(['status'])?.patchValue(2);
    this.searchForm.get(['subject'])?.patchValue('');
    this.searchForm.get(['docno'])?.patchValue('');
    this.meetingDeliveryList = [];
    this.meetingService.clearSearchCriteria(MEETING_SENT_KEY);
  }

  updateCriteriaData(criteria: ISearchCriteria): void {
    const startDate = dayjs(criteria.dateFrom, 'DD-MM-YYYY');
    const endDate = dayjs(criteria.dateTo, 'DD-MM-YYYY');

    this.searchForm.get('fromdate')?.patchValue(startDate);
    this.searchForm.get('todate')?.patchValue(endDate);
    this.searchForm.get('departmentID')?.patchValue(criteria.senderId);
    this.searchForm.get('status')?.patchValue(criteria.status);
    this.searchForm.get('subject')?.patchValue(criteria.subject);
    this.searchForm.get('docno')?.patchValue(criteria.referenceNo);
    this.searchMeetingInvitation();
  }

  createCriteriaData(): ISearchCriteria {
    const searchCriteria = {
      ...new SearchCriteria(),
      requestFrom: 2,
      dateFrom: this.searchForm.get(['fromdate'])!.value.format('DD-MM-YYYY'),
      dateTo: this.searchForm.get(['todate'])!.value.format('DD-MM-YYYY'),
      status: this.searchForm.get(['status'])!.value,
      senderId: this.searchForm.get(['departmentID'])!.value,
      subject: this.searchForm.get(['subject'])!.value,
      referenceNo: this.searchForm.get(['docno'])!.value,
    };

    return searchCriteria;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    if (this.searchForm.invalid) {
      this.isShowingResult = true;
      this.isShowingAlert = true;
    } else {
      const searchCriteria = this.createCriteriaData();
      const pageToLoad: number = page ?? this.page ?? 1;

      const requestParams = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        criteria: JSON.stringify(searchCriteria),
      };

      this.meetingService.findAllReceived(requestParams).subscribe(
        (res: HttpResponse<IMeetingDelivery[]>) => {
          const result = res.body;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);

          if (!res.ok) {
            console.log('Error Message :', res.headers.get('message'));
          }
        },
        error => {
          this.ngbPaginationPage = this.page ?? 1;
          const replyCode = ResponseCode.WARNING;
          const replyMsg = error.headers.get('message');
          if (replyMsg) {
            this.showAlertMessage(replyCode, replyMsg);
          }
        }
      );
    }
  }

  private showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  private onSuccess(data: IMeetingDelivery[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.meetingDeliveryList = data!;
    this.page = page;
    this.isShowingAlert = this.meetingDeliveryList.length === 0;
    this.ngbPaginationPage = this.page;
  }
}
