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
import { DeliveryService } from '../service/delivery.service';
import { IDocumentDelivery } from '../delivery.model';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { DELIVERY_RECEIVED_KEY } from 'app/config/input.constants';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-delivery-received',
  templateUrl: './delivery-received.component.html',
  styleUrls: ['./delivery-received.component.scss'],
})
export class DeliveryReceivedComponent implements OnInit, OnDestroy {
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
  documentDelivery?: IDocumentDelivery[];
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
    protected deliveryService: DeliveryService,
    protected userAuthorityService: UserAuthorityService
  ) {}

  ngOnInit(): void {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];

        const searchedCriteria = this.deliveryService.getSearchCriteria(DELIVERY_RECEIVED_KEY);
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
    this.deliveryService.clearPreviousState();
  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
  }

  searchDocumentDelivery(): void {
    if (this.searchForm.invalid) {
      this.isShowingResult = true;
      this.isShowingAlert = true;
    } else {
      this.loadPage(1);
    }
  }

  goToViewDetails(id?: number): void {
    this.deliveryService.storeSearchCriteria(DELIVERY_RECEIVED_KEY, this.createCriteriaData());
    this.router.navigate(['/delivery', id, 'view']);
  }

  updateCriteriaData(criteria: ISearchCriteria): void {
    const startDate = dayjs(criteria.dateFrom, 'DD-MM-YYYY');
    const endDate = dayjs(criteria.dateTo, 'DD-MM-YYYY');

    this.searchForm.get('fromdate')?.patchValue(startDate);
    this.searchForm.get('todate')?.patchValue(endDate);
    this.searchForm.get(['departmentID'])?.patchValue(criteria.senderId);
    this.searchForm.get('status')?.patchValue(criteria.status);
    this.searchForm.get('subject')?.patchValue(criteria.subject);
    this.searchForm.get('docno')?.patchValue(criteria.referenceNo);
    this.searchDocumentDelivery();
  }

  clearForm(): void {
    this.searchForm.get(['fromdate'])?.patchValue('');
    this.searchForm.get(['todate'])?.patchValue('');
    this.searchForm.get(['departmentID'])?.patchValue(0);
    this.searchForm.get(['status'])?.patchValue(2);
    this.searchForm.get(['subject'])?.patchValue('');
    this.searchForm.get(['docno'])?.patchValue('');
    this.documentDelivery = [];
    this.deliveryService.clearSearchCriteria(DELIVERY_RECEIVED_KEY);
  }

  createCriteriaData(): ISearchCriteria {
    const startDate = this.searchForm.get(['fromdate'])!.value.format('DD-MM-YYYY');
    const endDate = this.searchForm.get(['todate'])!.value.format('DD-MM-YYYY');

    const searchCriteria = {
      ...new SearchCriteria(),
      requestFrom: 2,
      dateFrom: startDate,
      dateTo: endDate,
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
      const pageToLoad: number = page ?? this.page;

      const searchCriteria = this.createCriteriaData();

      const requestParams = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        criteria: JSON.stringify(searchCriteria),
      };

      this.deliveryService.findAllReceived(requestParams).subscribe(
        (res: HttpResponse<IDocumentDelivery[]>) => {
          const result = res.body;
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

  protected onSuccess(data: IDocumentDelivery[] | null, headers: HttpHeaders, navigate: boolean, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.documentDelivery = data!;
    this.page = page;
    this.isShowingAlert = this.documentDelivery.length === 0;
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    //  this.ngbPaginationPage = this.page ?? 1;
  }
}
