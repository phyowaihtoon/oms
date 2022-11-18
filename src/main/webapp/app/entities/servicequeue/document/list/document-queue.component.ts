import { Component, OnInit } from '@angular/core';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { TranslateService } from '@ngx-translate/core';
import { IMenuItem } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { DocumentInquiry, IDocumentHeader } from 'app/entities/document/document.model';
import { DocumentInquiryService } from 'app/entities/document/service/document-inquiry.service';

@Component({
  selector: 'jhi-document-queue',
  templateUrl: './document-queue.component.html',
  styleUrls: ['./document-queue.component.scss'],
})
export class DocumentQueueComponent implements OnInit {
  _documentHeaders?: IDocumentHeader[];
  _metaDataHdrList?: IMetaDataHeader[] | null;
  _selectedMetaDataList?: IMetaData[];
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

  _metaData1 = { name: '', value: '', valid: false };
  _metaData2 = { name: '', value: '', valid: false };
  _metaData3 = { name: '', value: '', valid: false };
  _metaData4 = { name: '', value: '', valid: false };
  _metaData5 = { name: '', value: '', valid: false };

  searchForm = this.fb.group({
    metaDataHdrID: [],
    createdDate: [],
    fieldValues: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected documentInquiryService: DocumentInquiryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected loadSetupService: LoadSetupService,
    protected translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;
    });

    this.loadAllSetup();
    this.searchDocument(1);
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

  onChangeDocumentTemplate(event: any): void {
    const headerID: number = +this.searchForm.get('metaDataHdrID')!.value;
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === headerID);
    if (metaDataHeader) {
      this._selectedMetaDataList = metaDataHeader.metaDataDetails;
    }
  }

  bindMetaDataNames(): void {
    this.resetMetaDataNames();
    if (this._selectedMetaDataList !== undefined) {
      let arrIndex = 0;
      while (arrIndex < this._selectedMetaDataList.length) {
        if (arrIndex === 0) {
          this._metaData1.name = this._selectedMetaDataList[arrIndex].fieldName!;
          this._metaData1.valid = true;
        }
        if (arrIndex === 1) {
          this._metaData2.name = this._selectedMetaDataList[arrIndex].fieldName!;
          this._metaData2.valid = true;
        }
        if (arrIndex === 2) {
          this._metaData3.name = this._selectedMetaDataList[arrIndex].fieldName!;
          this._metaData3.valid = true;
        }
        if (arrIndex === 3) {
          this._metaData4.name = this._selectedMetaDataList[arrIndex].fieldName!;
          this._metaData4.valid = true;
        }
        if (arrIndex === 4) {
          this._metaData5.name = this._selectedMetaDataList[arrIndex].fieldName!;
          this._metaData5.valid = true;
        }

        arrIndex++;
      }
    }
  }

  resetMetaDataNames(): void {
    this._metaData1.name = '';
    this._metaData1.valid = false;
    this._metaData2.name = '';
    this._metaData2.valid = false;
    this._metaData3.name = '';
    this._metaData3.valid = false;
    this._metaData4.name = '';
    this._metaData4.valid = false;
    this._metaData5.name = '';
    this._metaData5.valid = false;
  }

  resetMetaDataValues(): void {
    this._metaData1.value = '';
    this._metaData2.value = '';
    this._metaData3.value = '';
    this._metaData4.value = '';
    this._metaData5.value = '';
  }

  bindMetaDataValues(fValues?: string): void {
    this.resetMetaDataValues();

    if (fValues !== undefined && fValues.trim().length > 0) {
      const fValueArray = fValues.split('|');
      if (fValueArray.length > 0) {
        let arrIndex = 0;
        while (arrIndex < fValueArray.length) {
          if (arrIndex === 0) {
            this._metaData1.value = fValueArray[arrIndex];
          }
          if (arrIndex === 1) {
            this._metaData2.value = fValueArray[arrIndex];
          }
          if (arrIndex === 2) {
            this._metaData3.value = fValueArray[arrIndex];
          }
          if (arrIndex === 3) {
            this._metaData4.value = fValueArray[arrIndex];
          }
          if (arrIndex === 4) {
            this._metaData5.value = fValueArray[arrIndex];
          }

          arrIndex++;
        }
      }
    }
  }

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  loadAllSetup(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this._metaDataHdrList = res.body;
      },
      error => {
        console.log('Response Failed : ', error);
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
      this.bindMetaDataNames();
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

    const searchCriteria = {
      ...new DocumentInquiry(),
      metaDataHeaderId: this.searchForm.get('metaDataHdrID')!.value,
      createdDate: this.searchForm.get('createdDate')!.value ? this.searchForm.get('createdDate')!.value.format('DD-MM-YYYY') : '',
      fieldValues: this.searchForm.get('fieldValues')!.value,
    };

    this.documentInquiryService.queryForQueue(searchCriteria, paginationReqParams).subscribe(
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
