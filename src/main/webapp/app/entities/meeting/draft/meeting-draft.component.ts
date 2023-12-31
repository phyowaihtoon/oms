import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IDepartment } from 'app/entities/department/department.model';
import { SearchCriteria } from 'app/entities/util/criteria.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { MeetingService } from '../service/meeting.service';
import { IMeetingDelivery, IReceiverInfo } from '../meeting.model';

@Component({
  selector: 'jhi-meeting-draft',
  templateUrl: './meeting-draft.component.html',
  styleUrls: ['./meeting-draft.component.scss'],
})
export class MeetingDraftComponent implements OnInit {
  isShowingFilters = true;
  isShowingResult = false;
  isShowingAlert = false;
  isLoading = false;

  totalItems = 10;
  itemsPerPage = ITEMS_PER_PAGE;
  page = 1;
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
      },
      error => {
        console.log(error);
      }
    );
  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
  }

  containCc(receiverList: IReceiverInfo[]): boolean {
    const ccDepartments = receiverList.filter(receiver => receiver.receiverType === 2);
    return ccDepartments.length > 0;
  }

  searchDocument(): void {
    if (this.searchForm.invalid) {
      // this.searchForm.get('metaDataHdrID')!.markAsTouched();
      this.isShowingResult = true;
      this.isShowingAlert = true;
      // this._alertMessage = this.translateService.instant('dmsApp.document.home.selectRequired');
    } else {
      this.loadPage(1);
    }
  }

  clearForm(): void {
    this.searchForm.get(['fromdate'])?.patchValue('');
    this.searchForm.get(['todate'])?.patchValue('');
    this.searchForm.get(['status'])?.patchValue(2);
    this.searchForm.get(['subject'])?.patchValue('');
    this.searchForm.get(['docno'])?.patchValue('');
    this.meetingDeliveryList = [];
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    if (this.searchForm.invalid) {
      // this.searchForm.get('departmentID')!.markAsTouched();
      this.isShowingResult = true;
      this.isShowingAlert = true;
      // this._alertMessage = this.translateService.instant('dmsApp.document.home.selectRequired');
    } else {
      const startDate = this.searchForm.get(['fromdate'])!.value.format('DD-MM-YYYY');
      const endDate = this.searchForm.get(['todate'])!.value.format('DD-MM-YYYY');
      const _status = this.searchForm.get(['status'])!.value;
      // const _receiverId = this.searchForm.get(['departmentID'])!.value;
      const _subject = this.searchForm.get(['subject'])!.value;
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
      const pageToLoad: number | undefined = page ?? this.page ?? 1;

      const Criteria = {
        ...new SearchCriteria(),
        requestFrom: 2,
        dateFrom: startDate,
        dateTo: endDate,
        status: _status,
        subject: _subject,
        referenceNo: this.searchForm.get(['docno'])!.value,
      };

      console.log(Criteria, 'xxx Criteria xxxx');

      const requestParams = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        criteria: JSON.stringify(Criteria),
      };

      console.log(requestParams, 'xxx requestparams xxxx');

      this.meetingService.findAllDraft(requestParams).subscribe(
        (res: HttpResponse<IMeetingDelivery[]>) => {
          const result = res.body;
          console.log(result, ' xxxxx result xxxxxxxx');
          this.onSuccess(res.body, res.headers, !dontNavigate, pageToLoad);

          if (!res.ok) {
            console.log('Error Message :', res.headers.get('message'));
          }
        },
        res => {
          console.log('Error Message :', res.headers.get('message'));
        }
      );
    }
  }

  protected onSuccess(data: IMeetingDelivery[] | null, headers: HttpHeaders, navigate: boolean, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.meetingDeliveryList = data!;
    this.page = page;
    this.isShowingAlert = this.meetingDeliveryList.length === 0;
    // this._alertMessage = this.translateService.instant('dmsApp.document.home.notFound');
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    //  this.ngbPaginationPage = this.page ?? 1;
  }
}
