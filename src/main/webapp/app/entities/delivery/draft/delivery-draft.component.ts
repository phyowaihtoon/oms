
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
import { DeliveryRoutingResolveService } from '../route/delivery-routing-resolve.service';
import { DeliveryService } from '../service/delivery.service';
import { IDocumentDelivery } from '../delivery.model';

@Component({
  selector: 'jhi-delivery-draft',
  templateUrl: './delivery-draft.component.html',
  styleUrls: ['./delivery-draft.component.scss'],
})
export class DeliveryDraftComponent implements OnInit{

  isShowingFilters = true;
  isShowingResult = false;
  isShowingAlert = false;
  isLoading = false;

  totalItems = 10;
  itemsPerPage = ITEMS_PER_PAGE;
  //page?: number;
  page = 1;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;  
  departmentsList?: IDepartment[];
  documentDelivery?: IDocumentDelivery[];


  searchForm = this.fb.group({
    fromdate: [],    
    todate: [],
    subject: [],
    departmentID: [0, [Validators.required, Validators.pattern('^[1-9]*$')]],
    status: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected translateService: TranslateService,    
    protected loadSetupService: LoadSetupService,
    protected deliveryService: DeliveryService,
  ) {

  }
  
  ngOnInit(): void {

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];
      },
      () => {
        // error;
      }
    );

  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
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
  loadPage(page?: number, dontNavigate?: boolean): void {
   
    if (this.searchForm.invalid) {
      this.searchForm.get('departmentID')!.markAsTouched();
      this.isShowingResult = true;
      this.isShowingAlert = true;
      // this._alertMessage = this.translateService.instant('dmsApp.document.home.selectRequired');
    } else {
      
      const startDate = this.searchForm.get(['fromdate'])!.value.format('DD-MM-YYYY');
      const endDate = this.searchForm.get(['todate'])!.value.format('DD-MM-YYYY');
      const _status = this.searchForm.get(['status'])!.value;
      // const _receiverId = this.searchForm.get(['departmentID'])!.value;
      const _subject = this.searchForm.get(['subject'])!.value;


        const Criteria={
          ...new SearchCriteria(),
        requestFrom: 2,
        dateFrom: startDate,
        dateTo: endDate,
        status: _status,
        // receiverId: _receiverId,
        subject: _subject,
      };
    
      console.log(Criteria, "xxx Criteria xxxx");

      const requestParams={
            page:1,
            size: 1,
            criteria:JSON.stringify(Criteria)
          };         
          
      console.log(requestParams, "xxx requestparams xxxx")

      this.deliveryService.findAllDraft(requestParams)
        .subscribe(
          (res: HttpResponse<IDocumentDelivery[]>) => {

            const result = res.body;
            console.log(result, " xxxxx result xxxxxxxx")
            this.onSuccess(res.body, res.headers, !dontNavigate);
      
            if(!res.ok){
              console.log("Error Message :", res.headers.get('message'));
            }
          }, 
          (res) => {
            console.log("Error Message :", res.headers.get('message'));
          }
        );
      }
  }

  protected onSuccess(data: IDocumentDelivery[] | null, headers: HttpHeaders, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.documentDelivery = data!;
    this.isShowingAlert = this.documentDelivery.length === 0;
    // this._alertMessage = this.translateService.instant('dmsApp.document.home.notFound');
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
   //  this.ngbPaginationPage = this.page ?? 1;
  }


 
  }

