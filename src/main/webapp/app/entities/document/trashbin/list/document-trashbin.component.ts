import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IDocumentStatus, IMenuItem } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { DocumentInquiry, IDocumentHeader, IDocumentInquiry } from '../../document.model';
import { DocumentInquiryService } from '../../service/document-inquiry.service';

@Component({
  selector: 'jhi-document-trashbin',
  templateUrl: './document-trashbin.component.html',
  styleUrls: ['./document-trashbin.component.scss'],
})
export class DocumentTrashbinComponent implements OnInit, OnDestroy {
  _documentHeaders?: IDocumentHeader[];
  _metaDataHdrList?: IMetaDataHeader[] | null;
  _documentStatusList?: IDocumentStatus[];
  _selectedMetaDataList?: IMetaData[];
  _metaDataColumns?: IMetaData[];
  _displayedMetaDataColumns?: IMetaData[];
  _displayedMetaDataValues?: string[] = [];
  _staticMetaDataColumns = [
    { fieldName: 'DS', translateKey: 'dmsApp.document.status', isDisplayed: true },
    { fieldName: 'CD', translateKey: 'dmsApp.document.createdDate', isDisplayed: true },
    { fieldName: 'CB', translateKey: 'dmsApp.document.createdBy', isDisplayed: true },
  ];
  _lovValuesF1?: string[] = [];
  _lovValuesF2?: string[] = [];
  _lovValuesF3?: string[] = [];
  isLOV1 = false;
  isLOV2 = false;
  isLOV3 = false;
  _fieldLabel1?: string = '';
  _fieldLabel2?: string = '';
  _fieldLabel3?: string = '';
  _fieldOrder1?: number = 0;
  _fieldOrder2?: number = 0;
  _fieldOrder3?: number = 0;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  isShowingFilters = true;
  isShowingResult = false;
  isShowingAlert = false;
  _alertMessage = '';

  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;

  searchForm = this.fb.group({
    metaDataHdrID: [0, [Validators.required, Validators.pattern('^[1-9]*$')]],
    createdDate: [],
    fieldValue1: [{ value: '', disabled: true }],
    fieldValue2: [{ value: '', disabled: true }],
    fieldValue3: [{ value: '', disabled: true }],
    generalValue: [],
  });

  _searchCriteria?: IDocumentInquiry;

  constructor(
    protected fb: FormBuilder,
    protected documentInquiryService: DocumentInquiryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected loadSetupService: LoadSetupService,
    protected translateService: TranslateService
  ) {}

  ngOnDestroy(): void {
    this.documentInquiryService.clearPreviousState();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;
      this.loadAllSetup();
    });
  }

  trackDocumentHeaderById(index: number, item: IDocumentHeader): number {
    return item.id!;
  }

  trackMetaDataByID(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  showFilters(): void {
    this.isShowingFilters = !this.isShowingFilters;
  }

  showResultArea(): void {
    this.isShowingResult = !this.isShowingResult;
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
    const headerID: number = +this.searchForm.get('metaDataHdrID')!.value;
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === headerID);
    if (metaDataHeader) {
      this._selectedMetaDataList = metaDataHeader.metaDataDetails;
      this.bindSearchCriteria();
      this.bindMetaDataColumns();
    }
  }

  bindSearchCriteria(): void {
    this.isLOV1 = false;
    this.isLOV2 = false;
    this.isLOV3 = false;
    this.searchForm.get('fieldValue1')?.patchValue('');
    this.searchForm.get('fieldValue2')?.patchValue('');
    this.searchForm.get('fieldValue3')?.patchValue('');
    this._fieldLabel1 = '';
    this._fieldLabel2 = '';
    this._fieldLabel3 = '';

    const searchFieldList = this._selectedMetaDataList?.filter(item => item.searchBy === 'Y');

    if (searchFieldList && searchFieldList.length > 0) {
      const metaData1 = searchFieldList[0];
      this.searchForm.get('fieldValue1')?.enable();
      this._fieldLabel1 = metaData1.fieldName;
      this._fieldOrder1 = metaData1.fieldOrder;
      if (metaData1.fieldType === 'LOV') {
        this.isLOV1 = true;
        this._lovValuesF1 = metaData1.fieldValue?.split('|');
      }

      if (searchFieldList.length > 1) {
        const metaData2 = searchFieldList[1];
        this.searchForm.get('fieldValue2')?.enable();
        this._fieldLabel2 = metaData2.fieldName;
        this._fieldOrder2 = metaData2.fieldOrder;
        if (metaData2.fieldType === 'LOV') {
          this.isLOV2 = true;
          this._lovValuesF2 = metaData2.fieldValue?.split('|');
        }
      }

      if (searchFieldList.length > 2) {
        const metaData3 = searchFieldList[2];
        this.searchForm.get('fieldValue3')?.enable();
        this._fieldLabel3 = metaData3.fieldName;
        this._fieldOrder3 = metaData3.fieldOrder;
        if (metaData3.fieldType === 'LOV') {
          this.isLOV3 = true;
          this._lovValuesF3 = metaData3.fieldValue?.split('|');
        }
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

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  loadAllSetup(): void {
    if (this._userAuthority?.roleID) {
      this.loadSetupService.loadAllMetaDataHeaderByUserRole(this._userAuthority.roleID).subscribe(
        (res: HttpResponse<IMetaDataHeader[]>) => {
          if (res.body) {
            this._metaDataHdrList = res.body;
            const searchedCriteria = this.documentInquiryService.getSearchCriteria();
            if (searchedCriteria) {
              this.updateSearchFormData(searchedCriteria);
            }
          }
        },
        error => {
          console.log('Loading MetaData Header Failed : ', error);
        }
      );
    }

    this.loadSetupService.loadDocumentStatus().subscribe(
      (res: HttpResponse<IDocumentStatus[]>) => {
        if (res.body) {
          this._documentStatusList = res.body;
        }
      },
      error => {
        console.log('Loading Document Status Failed : ', error);
      }
    );
  }

  searchDocument(page?: number): void {
    if (this.searchForm.invalid) {
      this.searchForm.get('metaDataHdrID')!.markAsTouched();
      this.isShowingResult = true;
      this.isShowingAlert = true;
      this._alertMessage = this.translateService.instant('dmsApp.document.home.selectRequired');
    } else {
      this.loadPage(page);
    }
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    this.isShowingResult = true;
    this._documentHeaders = [];
    const pageToLoad: number = page ?? this.page ?? 1;
    const paginationReqParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      // sort: this.sort(),
    };

    this._searchCriteria = {
      ...new DocumentInquiry(),
      metaDataHeaderId: this.searchForm.get('metaDataHdrID')!.value,
      createdDate: this.searchForm.get('createdDate')!.value ? this.searchForm.get('createdDate')!.value.format('DD-MM-YYYY') : '',
      fieldValue1: this.searchForm.get('fieldValue1')!.value,
      fieldIndex1: this._fieldOrder1,
      fieldValue2: this.searchForm.get('fieldValue2')!.value,
      fieldIndex2: this._fieldOrder2,
      fieldValue3: this.searchForm.get('fieldValue3')!.value,
      fieldIndex3: this._fieldOrder2,
      generalValue: this.searchForm.get('generalValue')!.value,
    };

    this.documentInquiryService.searchInTrashBin(this._searchCriteria, paginationReqParams).subscribe(
      (res: HttpResponse<IDocumentHeader[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  clearFormData(): void {
    this.searchForm.reset();
    this._documentHeaders = [];
    this.isShowingResult = false;
    this.isLOV1 = false;
    this.isLOV2 = false;
    this.isLOV3 = false;
    this._fieldLabel1 = '';
    this._fieldLabel2 = '';
    this._fieldLabel3 = '';
    this._selectedMetaDataList = [];
    this.searchForm.get('metaDataHdrID')?.patchValue(0);
    this.searchForm.get('fieldValue1')?.disable();
    this.searchForm.get('fieldValue2')?.disable();
    this.searchForm.get('fieldValue3')?.disable();
    this.documentInquiryService.clearSearchCriteria();
  }

  updateSearchFormData(criteriaData: IDocumentInquiry): void {
    this.searchForm.get('metaDataHdrID')?.patchValue(criteriaData.metaDataHeaderId);
    this.onChangeDocumentTemplate();
    this.searchForm.get('fieldValue1')?.patchValue(criteriaData.fieldValue1);
    this.searchForm.get('fieldValue2')?.patchValue(criteriaData.fieldValue2);
    this.searchForm.get('fieldValue3')?.patchValue(criteriaData.fieldValue3);
    this.searchForm.get('generalValue')?.patchValue(criteriaData.generalValue);
    this.searchDocument(1);
  }

  goToView(id?: number): void {
    this.documentInquiryService.storeSearchCriteria(this._searchCriteria);
    this.router.navigate(['/document/trashbin', id, 'view']);
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
    this.isShowingAlert = this._documentHeaders.length === 0;
    this._alertMessage = this.translateService.instant('dmsApp.document.home.notFound');
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
