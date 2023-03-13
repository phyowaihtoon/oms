import { FormBuilder, Validators } from '@angular/forms';
import { DashboardService } from './../services/dashboard-service';
import { Component, Input, OnInit, AfterViewInit, AfterContentInit, enableProdMode } from '@angular/core';
import { SchowChartService } from './showchart.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { IPieHeaderDataDto } from 'app/services/pieheaderdata.model';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { ActivatedRoute } from '@angular/router';
import { IUserAuthority } from 'app/login/userauthority.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IInputParam, InputParam } from 'app/services/input-param.model';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { DocumentInquiry, IDocumentHeader, IDocumentInquiry } from 'app/entities/document/document.model';
import { DocumentInquiryService } from 'app/entities/document/service/document-inquiry.service';
import { TranslateService } from '@ngx-translate/core';
import { IDocumentStatus, IMenuItem } from 'app/entities/util/setup.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
enableProdMode();
@Component({
  selector: 'jhi-showchart',
  templateUrl: './showchart.component.html',
  styleUrls: ['./showchart.scss'],
})
export class ShowChartComponent implements OnInit, AfterViewInit {
  @Input() template: any;
  totalCount = 0;

  _documentHeaders?: IDocumentHeader[];
  _metaDataHdrList?: IMetaDataHeader[] | null;
  _documentStatusList?: IDocumentStatus[];
  _selectedMetaDataList?: IMetaData[];
  _metaDataColumns?: IMetaData[];
  _displayedMetaDataColumns?: IMetaData[];
  _displayedMetaDataValues?: string[] = [];
  _staticMetaDataColumns: any;
  _lovValuesF1?: string[] = [];
  _lovValuesF2?: string[] = [];
  _lovValuesF3?: string[] = [];
  isLOV1 = false;
  isLOV2 = false;
  isLOV3 = false;
  _fieldLabel1?: string = '';
  _fieldLabel2?: string = '';
  _fieldLabel3?: string = '';
  _fieldOrder1: number = 0;
  _fieldOrder2: number = 0;
  _fieldOrder3: number = 0;

  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  _userAuthority?: IUserAuthority | null;

  editForm = this.fb.group({
    metaDataHdrID: [0, [Validators.required, Validators.pattern('^[1-9]*$')]],
    fromDate: [],
    toDate: [],
    createdDate: [],
    fieldValue1: [{ value: '', disabled: true }],
    fieldValue2: [{ value: '', disabled: true }],
    fieldValue3: [{ value: '', disabled: true }],
    generalValue: [],
    docStatus: [0],
    pageNo: [],
  });

  _searchCriteria?: IDocumentInquiry;

  constructor(
    protected fb: FormBuilder,
    private dashboard: DashboardService,
    private showChart: SchowChartService,
    protected userAuthorityService: UserAuthorityService,
    protected modalService: NgbModal,
    protected domSanitizer: DomSanitizer,
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService,
    protected documentInquiryService: DocumentInquiryService,
    protected translateService: TranslateService
  ) {}

  ngAfterViewInit(): void {
    this.showData();
  }

  ngOnInit(): void {
    this._userAuthority = this.userAuthorityService.retrieveUserAuthority();
    if (this.template.cardId !== 'CARD008') {
      this.loadAllSetup();
    }

    if (this.template.cardId === 'CARD008') {
      this.activatedRoute.data.subscribe(({ userAuthority }) => {
        this._staticMetaDataColumns = [
          { fieldName: 'ID', translateKey: 'global.field.id', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
          { fieldName: 'DN', translateKey: 'dmsApp.document.docTitle', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
          { fieldName: 'DS', translateKey: 'dmsApp.document.status', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
          { fieldName: 'CD', translateKey: 'dmsApp.document.createdDate', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
          { fieldName: 'CB', translateKey: 'dmsApp.document.createdBy', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
        ];
      });
      this.loadAllSetup2();
    }
  }

  today(): any {
    const todayDate = new Date();
    return {
      year: todayDate.getFullYear(),
      month: todayDate.getMonth() + 1,
      day: todayDate.getDate(),
    };
  }

  getTotalRecordCount(): string {
    return 'Total - ' + this.totalCount.toString() + (this.totalCount > 0 ? ' Records' : ' Record');
  }

  loadAllSetup(): void {
    if (this._userAuthority?.roleID) {
      this.loadSetupService
        .loadAllMetaDataHeaderByUserRole(this._userAuthority.roleID)
        .subscribe((res: HttpResponse<IMetaDataHeader[]>) => {
          if (res.body) {
            this._metaDataHdrList = res.body;
            if (this._metaDataHdrList.length > 0) {
              this.editForm.get(['metaDataHdrID'])?.patchValue(this._metaDataHdrList[0].id);
              this.onChangeDocumentTemplate();
            }
          }
        });
    }
  }

  trackDocumentHeaderById(index: number, item: IDocumentHeader): number {
    return item.id!;
  }

  trackMetaDataByID(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  getDocTitleByID(id?: number): string | undefined {
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === id);
    return metaDataHeader?.docTitle;
  }

  getDocStatusDesc(value?: number): string | undefined {
    const documentStatus = this._documentStatusList?.find(item => item.value === value);
    return documentStatus?.description;
  }

  onChangeDocumentTemplate(): void {
    this.showData();
  }

  showData(): any {
    /*
    const innerEle = document.getElementById(this.template.cardId);
    if(innerEle !==null){
      innerEle.innerHTML = '' ;
    }
    */

    if (this.template.cardId === 'CARD002') {
      this.showChart.getAllSummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body), '');
        }
      });
    }

    if (this.template.cardId === 'CARD003') {
      this.showChart.getTodaySummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body), '');
        }
      });
    }

    if (this.template.cardId === 'CARD004') {
      const inputParam = this.createParam();
      this.showChart.getDataByTemplate(inputParam).subscribe((res: HttpResponse<[]>) => {
        if (res.body) {
          const cols: any = [];
          // tslint:disable-next-line: typedef
          res.body.forEach(function (e: any) {
            // tslint:disable-next-line: typedef
            e.detail.forEach(function (d: any) {
              // tslint:disable-next-line: typedef
              if (
                cols.filter(function (t: any) {
                  return t === d.name;
                }).length === 0
              ) {
                cols.push(d.name);
              }
            });
          });
          this.dashboard.generateLineChart(this.template.cardId, this.prepareData(res.body, cols), cols, 'Count', '');
        }
      });
    }

    if (this.template.cardId === 'CARD005') {
      const inputParam = this.createParam();
      this.showChart.getTodaySummaryByTemplate(inputParam).subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body), '');
        }
      });
    }

    if (this.template.cardId === 'CARD006') {
      this.totalCount = 0;
      const inputParam = this.createParam();
      this.showChart.getDataByTemplateType(inputParam).subscribe((res: HttpResponse<[]>) => {
        if (res.body) {
          const cols: any = [];
          const data: any[] = [];
          // tslint:disable-next-line: typedef
          res.body.forEach((e: any) => {
            this.totalCount = this.totalCount + Number(e.detail.count);
            if (
              cols.filter(function (t: any) {
                return t === e.detail.name;
              }).length === 0
            ) {
              cols.push(e.detail.name);
              data.push(e.detail.count);
            }
          });

          this.dashboard.generateSingleBarChart(this.template.cardId, data, cols, 'Record Count', '');
        }
      });
    }

    if (this.template.cardId === 'CARD007') {
      this.showChart.getOverallSummaryByTemplate().subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body), '');
        }
      });
    }

    if (this.template.cardId === 'CARD008') {
      //this.searchDocument();
    }
  }

  prepareData2(data: any, cols: any): any {
    this.totalCount = 0;
    const _tempObj: { name: any; data: any; color: any }[] = [];
    data.forEach((d: any) => {
      this.totalCount = this.totalCount + Number(d.detail.count);
      const count: any = [];
      cols.forEach((e: any) => {
        let value = 0;
        if (e === d.detail.name) {
          value = d.detail.count;
        }
        if (value !== 0) {
          count.push(value);
        } else {
          count.push(null);
        }
      });
      _tempObj.push({ name: d.type, data: count, color: '#c97530' });
    });
    return _tempObj;
  }

  prepareData(data: any, cols: any): any {
    const _tempObj: { name: any; data: any }[] = [];
    data.forEach((d: any) => {
      const count: any = [];
      cols.forEach((e: any) => {
        let value = 0;
        d.detail.forEach((f: any) => {
          if (e === f.name) {
            value = f.count;
          }
        });
        count.push(value);
      });
      _tempObj.push({ name: d.type, data: count });
    });
    return _tempObj;
  }
  createParam(): IInputParam {
    return {
      ...new InputParam(),
      templateId: this.editForm.get(['metaDataHdrID'])!.value,
      // fromDate: this.editForm.get('fromDate')!.value ? this.editForm.get('fromDate')!.value.format('DD-MM-YYYY') : '',
      // toDate: this.editForm.get('toDate')!.value ? this.editForm.get('toDate')!.value.format('DD-MM-YYYY') : '',
    };
  }

  preparePieData(req: any): any {
    this.totalCount = req.totalCount;
    const ret: any = [];
    req.data?.forEach((d: any) => {
      ret.push({ name: d.name, y: d.data });
    });

    return [
      {
        name: 'Documents',
        colorByPoint: true,
        data: ret,
      },
    ];
  }

  preparePieDataOld(): any {
    return [
      {
        name: 'Documents',
        colorByPoint: true,
        data: [
          {
            name: 'New - 10 Records',
            y: 65.5,
            sliced: true,
            selected: true,
          },
          {
            name: 'Sent for Approval - 15 Records',
            y: 11.3,
          },
          {
            name: 'Sent for Amendment - 12 Records',
            y: 2.5,
          },
          {
            name: 'Approved - 2 Records',
            y: 4.6,
          },
          {
            name: 'Rejected - 1 Record',
            y: 7.2,
          },
          {
            name: 'Canceled - 5 Records',
            y: 8.9,
          },
        ],
      },
    ];
  }

  /*
  prepareData(data:any, cols:any):any {
    const _tempObj :any = [];
    data.forEach((d:any) => {
      const amount :any = [];
      cols.forEach((e:any) => {
        let value = 0;
        d.detail.forEach((f:any) => {
          if (e === f.name) {
            value = f.amount;
          }
        });
        amount.push(value);
      });
      _tempObj.push({ name: d.type, data: amount });
    });
    return _tempObj;
  }
  */

  onChangeDocumentTemplate2(): void {
    this.clearSearchCriteriaData();
    const headerID: number = +this.editForm.get('metaDataHdrID')!.value;
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === headerID);
    if (metaDataHeader) {
      this._selectedMetaDataList = metaDataHeader.metaDataDetails;
      this.bindSearchCriteria();
      this.bindMetaDataColumns();
      this.searchDocument();
    }
  }

  bindDefaultDepartment(): void {
    if (this._userAuthority?.departmentId) {
      this.editForm.get('metaDataHdrID')?.patchValue(this._userAuthority.departmentId);
      const metaDataHeader = this._metaDataHdrList?.find(item => item.id === this._userAuthority?.departmentId);
      if (metaDataHeader) {
        this._selectedMetaDataList = metaDataHeader.metaDataDetails;
        this.bindSearchCriteria();
        this.bindMetaDataColumns();
      }
      this.searchDocument();
    }
  }

  bindSearchCriteria(): void {
    this.isLOV1 = false;
    this.isLOV2 = false;
    this.isLOV3 = false;
    this.editForm.get('fieldValue1')?.patchValue('');
    this.editForm.get('fieldValue2')?.patchValue('');
    this.editForm.get('fieldValue3')?.patchValue('');
    this._fieldLabel1 = '';
    this._fieldLabel2 = '';
    this._fieldLabel3 = '';

    const metaData1 = this._selectedMetaDataList?.find(item => item.fieldOrder === 1);
    if (metaData1) {
      this.editForm.get('fieldValue1')?.enable();
      this._fieldLabel1 = metaData1.fieldName;
      this._fieldOrder1 = 1;
      if (metaData1.fieldType === 'LOV') {
        this.isLOV1 = true;
        this._lovValuesF1 = metaData1.fieldValue?.split('|');
      }
    }

    const metaData2 = this._selectedMetaDataList?.find(item => item.fieldOrder === 2);
    if (metaData2) {
      this.editForm.get('fieldValue2')?.enable();
      this._fieldLabel2 = metaData2.fieldName;
      this._fieldOrder2 = 2;
      if (metaData2.fieldType === 'LOV') {
        this.isLOV2 = true;
        this._lovValuesF2 = metaData2.fieldValue?.split('|');
      }
    }

    const metaData3 = this._selectedMetaDataList?.find(item => item.fieldOrder === 3);
    if (metaData3) {
      this.editForm.get('fieldValue3')?.enable();
      this._fieldLabel3 = metaData3.fieldName;
      this._fieldOrder3 = 3;
      if (metaData3.fieldType === 'LOV') {
        this.isLOV3 = true;
        this._lovValuesF3 = metaData3.fieldValue?.split('|');
      }
    }
  }

  showHideStaticColumn(staticData: any): void {
    staticData.isDisplayed = staticData.isDisplayed === undefined ? true : !staticData.isDisplayed;
  }

  showHideColumn(metaData: IMetaData): void {
    metaData.isDisplayed = metaData.isDisplayed === undefined ? true : !metaData.isDisplayed;
    this._displayedMetaDataColumns = this._metaDataColumns?.filter(item => item.isDisplayed === true);
  }

  bindMetaDataColumns(): void {
    this._displayedMetaDataColumns = [];
    this._metaDataColumns = this._selectedMetaDataList;
    this._metaDataColumns?.forEach((value, index) => {
      // Initially, the first five metadata fields will be shown in list
      if (index < 5) {
        value.isDisplayed = true;
        this._displayedMetaDataColumns?.push(value);
      }
    });
  }

  bindMetaDataValues(fValues?: string): void {
    this._displayedMetaDataValues = [];
    if (fValues !== undefined && fValues.trim().length > 0) {
      const fValueArray = fValues.split('|');
      if (fValueArray.length > 0) {
        this._displayedMetaDataColumns?.forEach(item => {
          if (item.fieldOrder) {
            const fieldValue = fValueArray[item.fieldOrder - 1];
            this._displayedMetaDataValues?.push(fieldValue);
          }
        });
      }
    }
  }

  loadAllSetup2(): void {
    if (this._userAuthority?.roleID) {
      this.loadSetupService
        .loadAllMetaDataHeaderByUserRole(this._userAuthority.roleID)
        .subscribe((res: HttpResponse<IMetaDataHeader[]>) => {
          if (res.body) {
            this._metaDataHdrList = res.body;
            const searchedCriteria = this.documentInquiryService.getSearchCriteria();
            if (searchedCriteria) {
              this.updateSearchFormData(searchedCriteria);
            } else {
              this.bindDefaultDepartment();
            }
          }
        });
    }

    this.loadSetupService.loadDocumentStatus().subscribe((res: HttpResponse<IDocumentStatus[]>) => {
      if (res.body) {
        this._documentStatusList = res.body;
      }
    });
  }

  searchDocument(): void {
    const pageNo = this.editForm.get('pageNo')!.value;
    if (pageNo && pageNo > 0) {
      this.loadPage(pageNo);
    } else {
      this.loadPage(1);
    }
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this._documentHeaders = [];
    const pageToLoad: number = page ?? this.page ?? 1;
    const paginationReqParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      // sort: this.sort(),
    };

    this._searchCriteria = {
      ...new DocumentInquiry(),
      metaDataHeaderId: this.editForm.get('metaDataHdrID')!.value,
      createdDate: this.editForm.get('createdDate')!.value ? this.editForm.get('createdDate')!.value.format('DD-MM-YYYY') : '',
      fieldValue1: this.editForm.get('fieldValue1')!.value,
      fieldIndex1: this._fieldOrder1,
      fieldValue2: this.editForm.get('fieldValue2')!.value,
      fieldIndex2: this._fieldOrder2,
      fieldValue3: this.editForm.get('fieldValue3')!.value,
      fieldIndex3: this._fieldOrder3,
      generalValue: this.editForm.get('generalValue')!.value,
      status: this.editForm.get('docStatus')!.value,
    };

    this.documentInquiryService.query(this._searchCriteria, paginationReqParams).subscribe(
      (res: HttpResponse<IDocumentHeader[]>) => {
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.onError();
      }
    );
  }

  clearSearchCriteriaData(): void {
    this._documentHeaders = [];
    this.isLOV1 = false;
    this.isLOV2 = false;
    this._selectedMetaDataList = [];
    this.editForm.get('fieldValue1')?.patchValue('');
    this.editForm.get('fieldValue1')?.disable();
    this.editForm.get('fieldValue2')?.patchValue('');
    this.editForm.get('fieldValue2')?.disable();
    this.editForm.get('fieldValue3')?.patchValue('');
    this.editForm.get('fieldValue3')?.disable();
    this.editForm.get('docStatus')?.patchValue(0);
    this.editForm.get('pageNo')?.patchValue('');
  }

  updateSearchFormData(criteriaData: IDocumentInquiry): void {
    this.editForm.get('metaDataHdrID')?.patchValue(criteriaData.metaDataHeaderId);
    this.onChangeDocumentTemplate();
    this.editForm.get('fieldValue1')?.patchValue(criteriaData.fieldValue1);
    this.editForm.get('fieldValue2')?.patchValue(criteriaData.fieldValue2);
    this.editForm.get('fieldValue3')?.patchValue(criteriaData.fieldValue3);
    this.editForm.get('generalValue')?.patchValue(criteriaData.generalValue);
    this.editForm.get('docStatus')?.patchValue(criteriaData.status);
    this.searchDocument();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IDocumentHeader[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this._documentHeaders = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
