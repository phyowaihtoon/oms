import { Component, OnInit, OnDestroy } from '@angular/core';
import { DocumentInquiry, IDocumentHeader, IDocumentInquiry } from '../document.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { DocumentInquiryService } from '../service/document-inquiry.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { IDocumentStatus, IMenuItem } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss'],
})
export class DocumentComponent implements OnInit, OnDestroy {
  _documentHeaders?: IDocumentHeader[];
  _metaDataHdrList?: IMetaDataHeader[] | null;
  _documentStatusList?: IDocumentStatus[];
  _selectedMetaDataList?: IMetaData[];
  _metaDataColumns?: IMetaData[];
  _displayedMetaDataColumns?: IMetaData[];
  _displayedMetaDataValues?: string[] = [];
  _staticMetaDataColumns: any;

  _metaDataField1?: IMetaData;
  _metaDataField2?: IMetaData;
  _metaDataField3?: IMetaData;
  _metaDataField4?: IMetaData;

  _lovValuesF1?: string[] = [];
  _lovValuesF2?: string[] = [];
  _lovValuesF3?: string[] = [];
  _lovValuesF4?: string[] = [];

  isLOV1 = false;
  isLOV2 = false;
  isLOV3 = false;
  isLOV4 = false;

  _isShowSearchType1 = false;
  _isShowSearchType2 = false;
  _isShowSearchType3 = false;
  _isShowSearchType4 = false;

  _fieldOrder1?: number = 0;
  _fieldOrder2?: number = 0;
  _fieldOrder3?: number = 0;
  _fieldOrder4?: number = 0;

  _fieldSortBy1?: number = 0;
  _fieldSortBy2?: number = 0;
  _fieldSortBy3?: number = 0;
  _fieldSortBy4?: number = 0;

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
  _selectedLanguage: string = 'my';

  _searchTypeList = [
    { value: 'EQ', description: 'Equal' },
    { value: 'CO', description: 'Contain' },
    { value: 'SW', description: 'Start With' },
    { value: 'EW', description: 'End With' },
  ];

  searchForm = this.fb.group({
    metaDataHdrID: [0, [Validators.required, Validators.pattern('^[1-9]*$')]],
    createdDate: [],
    fieldValue1: [{ value: '', disabled: true }],
    fieldValue2: [{ value: '', disabled: true }],
    fieldValue3: [{ value: '', disabled: true }],
    fieldValue4: [{ value: '', disabled: true }],
    fieldSearchType1: ['EQ'],
    fieldSearchType2: ['EQ'],
    fieldSearchType3: ['EQ'],
    fieldSearchType4: ['EQ'],
    generalValue: [],
    docStatus: [0],
    pageNo: [],
  });

  _searchCriteria?: IDocumentInquiry;

  constructor(
    protected fb: FormBuilder,
    protected documentInquiryService: DocumentInquiryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected loadSetupService: LoadSetupService,
    protected translateService: TranslateService,
    protected modalService: NgbModal
  ) {}

  ngOnDestroy(): void {
    this.documentInquiryService.clearPreviousState();
  }

  ngOnInit(): void {
    this._selectedLanguage = this.translateService.currentLang;
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this._selectedLanguage = this.translateService.currentLang;
    });

    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;
      this._staticMetaDataColumns = [
        { fieldName: 'ID', translateKey: 'global.field.id', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
        { fieldName: 'DN', translateKey: 'dmsApp.document.docTitle', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
        { fieldName: 'DS', translateKey: 'dmsApp.document.status', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
        { fieldName: 'CD', translateKey: 'dmsApp.document.createdDate', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
        { fieldName: 'CB', translateKey: 'dmsApp.document.createdBy', isDisplayed: this._userAuthority?.roleType === 1 ? true : false },
      ];
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
    this.clearSearchCriteriaData();
    const headerID: number = +this.searchForm.get('metaDataHdrID')!.value;
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === headerID);
    if (metaDataHeader) {
      this._selectedMetaDataList = metaDataHeader.metaDataDetails;
      this.bindSearchCriteria();
      this.bindMetaDataColumns();
    }
  }

  bindDefaultDepartment(): void {
    if (this._userAuthority?.departmentId) {
      this.searchForm.get('metaDataHdrID')?.patchValue(this._userAuthority.departmentId);
      const metaDataHeader = this._metaDataHdrList?.find(item => item.id === this._userAuthority?.departmentId);
      if (metaDataHeader) {
        this._selectedMetaDataList = metaDataHeader.metaDataDetails;
        this.bindSearchCriteria();
        this.bindMetaDataColumns();
      }
    }
  }

  bindSearchCriteria(): void {
    this.isLOV1 = false;
    this.isLOV2 = false;
    this.isLOV3 = false;
    this.isLOV4 = false;

    this._isShowSearchType1 = false;
    this._isShowSearchType2 = false;
    this._isShowSearchType3 = false;
    this._isShowSearchType4 = false;

    this._metaDataField1 = undefined;
    this._metaDataField2 = undefined;
    this._metaDataField3 = undefined;
    this._metaDataField4 = undefined;

    this.searchForm.get('fieldValue1')?.patchValue('');
    this.searchForm.get('fieldValue2')?.patchValue('');
    this.searchForm.get('fieldValue3')?.patchValue('');
    this.searchForm.get('fieldValue4')?.patchValue('');

    this._fieldSortBy1 = 0;
    this._fieldSortBy2 = 0;
    this._fieldSortBy3 = 0;
    this._fieldSortBy4 = 0;

    const searchFieldList = this._selectedMetaDataList?.filter(item => item.searchBy === 'Y');

    if (searchFieldList && searchFieldList.length > 0) {
      this._metaDataField1 = searchFieldList[0];
      this.searchForm.get('fieldValue1')?.enable();
      this._fieldOrder1 = this._metaDataField1.fieldOrder;
      if (this._metaDataField1.fieldType === 'LOV') {
        this.isLOV1 = true;
        this._lovValuesF1 = this._metaDataField1.fieldValue?.split('|');
      }
      if (this._metaDataField1.searchType === 'Y') {
        this._isShowSearchType1 = true;
      }

      if (searchFieldList.length > 1) {
        this._metaDataField2 = searchFieldList[1];
        this.searchForm.get('fieldValue2')?.enable();
        this._fieldOrder2 = this._metaDataField2.fieldOrder;
        if (this._metaDataField2.fieldType === 'LOV') {
          this.isLOV2 = true;
          this._lovValuesF2 = this._metaDataField2.fieldValue?.split('|');
        }
        if (this._metaDataField2.searchType === 'Y') {
          this._isShowSearchType2 = true;
        }
      }

      if (searchFieldList.length > 2) {
        this._metaDataField3 = searchFieldList[2];
        this.searchForm.get('fieldValue3')?.enable();
        this._fieldOrder3 = this._metaDataField3.fieldOrder;
        if (this._metaDataField3.fieldType === 'LOV') {
          this.isLOV3 = true;
          this._lovValuesF3 = this._metaDataField3.fieldValue?.split('|');
        }
        if (this._metaDataField3.searchType === 'Y') {
          this._isShowSearchType3 = true;
        }
      }

      if (searchFieldList.length > 3) {
        this._metaDataField4 = searchFieldList[3];
        this.searchForm.get('fieldValue4')?.enable();
        this._fieldOrder4 = this._metaDataField4.fieldOrder;
        if (this._metaDataField4.fieldType === 'LOV') {
          this.isLOV4 = true;
          this._lovValuesF4 = this._metaDataField4.fieldValue?.split('|');
        }
        if (this._metaDataField4.searchType === 'Y') {
          this._isShowSearchType4 = true;
        }
      }
    }

    const sortByList = this._selectedMetaDataList?.filter(item => item.sortBy === 'Y');
    if (sortByList && sortByList.length > 0) {
      const sortMetaData1 = sortByList[0];
      this._fieldSortBy1 = sortMetaData1.fieldOrder;
      if (sortByList.length > 1) {
        const sortMetaData2 = sortByList[1];
        this._fieldSortBy2 = sortMetaData2.fieldOrder;
      }
      if (sortByList.length > 2) {
        const sortMetaData3 = sortByList[2];
        this._fieldSortBy3 = sortMetaData3.fieldOrder;
      }
      if (sortByList.length > 3) {
        const sortMetaData4 = sortByList[3];
        this._fieldSortBy4 = sortMetaData4.fieldOrder;
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
            } else {
              this.bindDefaultDepartment();
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

  searchDocument(): void {
    if (this.searchForm.invalid) {
      this.searchForm.get('metaDataHdrID')!.markAsTouched();
      this.isShowingResult = true;
      this.isShowingAlert = true;
      this._alertMessage = this.translateService.instant('dmsApp.document.home.selectRequired');
    } else {
      const pageNo = this.searchForm.get('pageNo')!.value;
      if (pageNo && pageNo > 0) {
        this.loadPage(pageNo);
      } else {
        this.loadPage(1);
      }
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
      fieldIndex3: this._fieldOrder3,
      fieldValue4: this.searchForm.get('fieldValue4')!.value,
      fieldIndex4: this._fieldOrder4,
      fieldSearchType1: this.searchForm.get('fieldSearchType1')!.value,
      fieldSearchType2: this.searchForm.get('fieldSearchType2')!.value,
      fieldSearchType3: this.searchForm.get('fieldSearchType3')!.value,
      fieldSearchType4: this.searchForm.get('fieldSearchType4')!.value,
      fieldSortBy1: this._fieldSortBy1,
      fieldSortBy2: this._fieldSortBy2,
      fieldSortBy3: this._fieldSortBy3,
      fieldSortBy4: this._fieldSortBy4,
      generalValue: this.searchForm.get('generalValue')!.value,
      status: this.searchForm.get('docStatus')!.value,
    };

    this.documentInquiryService.query(this._searchCriteria, paginationReqParams).subscribe(
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
    this.isLOV4 = false;
    this._isShowSearchType1 = false;
    this._isShowSearchType2 = false;
    this._isShowSearchType3 = false;
    this._isShowSearchType4 = false;
    this._metaDataField1 = undefined;
    this._metaDataField2 = undefined;
    this._metaDataField3 = undefined;
    this._metaDataField4 = undefined;
    this._selectedMetaDataList = [];
    this.searchForm.get('metaDataHdrID')?.patchValue(0);
    this.searchForm.get('fieldValue1')?.disable();
    this.searchForm.get('fieldValue2')?.disable();
    this.searchForm.get('fieldValue3')?.disable();
    this.searchForm.get('fieldValue4')?.disable();
    this.searchForm.get('docStatus')?.patchValue(0);
    this.searchForm.get('pageNo')?.patchValue('');
    this.documentInquiryService.clearSearchCriteria();
  }

  clearSearchCriteriaData(): void {
    this._documentHeaders = [];
    this.isShowingResult = false;
    this.isLOV1 = false;
    this.isLOV2 = false;
    this._selectedMetaDataList = [];
    this.searchForm.get('fieldValue1')?.patchValue('');
    this.searchForm.get('fieldValue1')?.disable();
    this.searchForm.get('fieldValue2')?.patchValue('');
    this.searchForm.get('fieldValue2')?.disable();
    this.searchForm.get('fieldValue3')?.patchValue('');
    this.searchForm.get('fieldValue3')?.disable();
    this.searchForm.get('docStatus')?.patchValue(0);
    this.searchForm.get('pageNo')?.patchValue('');
  }

  updateSearchFormData(criteriaData: IDocumentInquiry): void {
    this.searchForm.get('metaDataHdrID')?.patchValue(criteriaData.metaDataHeaderId);
    this.onChangeDocumentTemplate();
    this.searchForm.get('fieldValue1')?.patchValue(criteriaData.fieldValue1);
    this.searchForm.get('fieldValue2')?.patchValue(criteriaData.fieldValue2);
    this.searchForm.get('fieldValue3')?.patchValue(criteriaData.fieldValue3);
    this.searchForm.get('generalValue')?.patchValue(criteriaData.generalValue);
    this.searchForm.get('docStatus')?.patchValue(criteriaData.status);
    this.searchDocument();
  }

  goToView(id?: number): void {
    this.documentInquiryService.storeSearchCriteria(this._searchCriteria);
    this.router.navigate(['/document', id, 'view']);
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
