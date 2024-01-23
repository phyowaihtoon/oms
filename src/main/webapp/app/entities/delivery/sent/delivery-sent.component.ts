import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DELIVERY_SENT_KEY } from 'app/config/input.constants';
import { IDepartment } from 'app/entities/department/department.model';
import { ISearchCriteria, SearchCriteria } from 'app/entities/util/criteria.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { DeliveryService } from '../service/delivery.service';
import { IDocumentDelivery, IReceiverInfo } from '../delivery.model';
import * as dayjs from 'dayjs';
import { UserAuthorityService } from 'app/login/userauthority.service';

@Component({
  selector: 'jhi-delivery-sent',
  templateUrl: './delivery-sent.component.html',
  styleUrls: ['./delivery-sent.component.scss'],
})
export class DeliverySentComponent implements OnInit, OnDestroy {
  isShowingAlert = false;
  alertMessage: string | null = 'No records found';
  isLoading = false;

  totalItems = 10;
  itemsPerPage = ITEMS_PER_PAGE;
  page = 1;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  departmentsList?: IDepartment[];
  documentDelivery?: IDocumentDelivery[];
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
    protected deliveryService: DeliveryService,
    protected userAuthorityService: UserAuthorityService
  ) {}

  ngOnInit(): void {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];

        const searchedCriteria = this.deliveryService.getSearchCriteria(DELIVERY_SENT_KEY);
        if (searchedCriteria) {
          this.updateCriteriaData(searchedCriteria);
        } else {
          const todayDate = dayjs().startOf('day');
          const defaultCriteria = {
            ...new SearchCriteria(),
            requestFrom: 2,
            dateFrom: todayDate.format('DD-MM-YYYY'),
            dateTo: todayDate.format('DD-MM-YYYY'),
            status: 2,
            subject: undefined,
            referenceNo: undefined,
          };
          this.updateCriteriaData(defaultCriteria);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnDestroy(): void {
    this.deliveryService.clearPreviousState();
  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
  }

  containCc(receiverList: IReceiverInfo[]): boolean {
    const ccDepartments = receiverList.filter(receiver => receiver.receiverType === 2);
    return ccDepartments.length > 0;
  }

  clearForm(): void {
    this.isShowingAlert = false;
    this.alertMessage = '';
    this.searchForm.get(['fromdate'])?.patchValue(null);
    this.searchForm.get(['todate'])?.patchValue(null);
    this.searchForm.get(['status'])?.patchValue(2);
    this.searchForm.get(['subject'])?.patchValue(null);
    this.searchForm.get(['docno'])?.patchValue(null);
    this.documentDelivery = [];
    this.deliveryService.clearSearchCriteria(DELIVERY_SENT_KEY);
  }

  goToViewDetails(id?: number): void {
    this.deliveryService.storeSearchCriteria(DELIVERY_SENT_KEY, this.createCriteriaData());
    this.router.navigate(['/delivery', id, 'view-sent']);
  }

  searchDocumentDelivery(): void {
    if (this.searchForm.invalid) {
      this.isShowingAlert = true;
    } else {
      this.loadPage(1);
    }
  }

  updateCriteriaData(criteria: ISearchCriteria): void {
    const startDate = dayjs(criteria.dateFrom, 'DD-MM-YYYY');
    const endDate = dayjs(criteria.dateTo, 'DD-MM-YYYY');

    this.searchForm.get('fromdate')?.patchValue(startDate);
    this.searchForm.get('todate')?.patchValue(endDate);
    this.searchForm.get('status')?.patchValue(criteria.status);
    this.searchForm.get('subject')?.patchValue(criteria.subject);
    this.searchForm.get('docno')?.patchValue(criteria.referenceNo);
    this.searchDocumentDelivery();
  }

  createCriteriaData(): ISearchCriteria {
    const startDate = this.searchForm.get(['fromdate'])!.value?.format('DD-MM-YYYY');
    const endDate = this.searchForm.get(['todate'])!.value?.format('DD-MM-YYYY');
    const _status = this.searchForm.get(['status'])!.value;
    const _subject = this.searchForm.get(['subject'])!.value;
    const _docno = this.searchForm.get(['docno'])!.value;

    const Criteria = {
      ...new SearchCriteria(),
      requestFrom: 2,
      dateFrom: startDate,
      dateTo: endDate,
      status: _status,
      subject: _subject,
      referenceNo: _docno,
    };

    return Criteria;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    if (this.searchForm.invalid) {
      this.isShowingAlert = true;
    } else {
      const pageToLoad: number = page ?? this.page;
      const searchCriteria = this.createCriteriaData();
      const requestParams = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        criteria: JSON.stringify(searchCriteria),
      };

      this.deliveryService.findAllSent(requestParams).subscribe(
        (res: HttpResponse<IDocumentDelivery[]>) => {
          this.onSuccess(res.body, res.headers, !dontNavigate, pageToLoad);

          if (!res.ok) {
            this.documentDelivery = [];
            this.isShowingAlert = true;
            this.alertMessage = res.headers.get('message');
          }
        },
        res => {
          this.documentDelivery = [];
          this.isShowingAlert = true;
          this.alertMessage = res.headers.get('message');
        }
      );
    }
  }

  protected onSuccess(data: IDocumentDelivery[] | null, headers: HttpHeaders, navigate: boolean, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.documentDelivery = data!;
    this.page = page;
    this.isShowingAlert = this.documentDelivery.length === 0;
    this.alertMessage = this.documentDelivery.length === 0 ? 'No records found' : '';
    this.ngbPaginationPage = this.page;
  }
}
