import { Component, OnInit } from '@angular/core';
import { DocumentInquiry, IDocumentHeader } from '../document.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { DocumentInquiryService } from '../service/document-inquiry.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss'],
})
export class DocumentComponent implements OnInit {
  documentHeaders?: IDocumentHeader[];
  metaDataHdrList?: IMetaDataHeader[] | null;
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

  searchForm = this.fb.group({
    metaDataHdrID: [null, [Validators.required]],
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
    this.loadAllSetup();
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
    const metaDataHeader = this.metaDataHdrList?.find(item => item.id === id);
    return metaDataHeader?.docTitle;
  }

  arrangeMetaData(fNames?: string, fValues?: string): string {
    let arrangedFields = '';
    if (fNames !== undefined && fValues !== undefined && fNames.trim().length > 0 && fValues.trim().length > 0) {
      const fNameArray = fNames.split('|');
      const fValueArray = fValues.split('|');
      if (fNameArray.length > 0 && fValueArray.length > 0 && fNameArray.length === fValueArray.length) {
        let arrIndex = 0;
        while (arrIndex < fNameArray.length) {
          arrangedFields += '<b>' + fNameArray[arrIndex] + '</b>' + ' : ' + fValueArray[arrIndex] + '&nbsp;&nbsp;';
          if ((arrIndex + 1) / 2 === 0) {
            arrangedFields += '<br>';
          }
          arrIndex++;
        }
      }
    }
    return arrangedFields;
  }

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  loadAllSetup(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.metaDataHdrList = res.body;
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
      return;
    }
    this.loadPage(page);
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    this.isShowingResult = true;
    this.documentHeaders = [];
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
    this.documentInquiryService.query(searchCriteria, paginationReqParams).subscribe(
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
    this.documentHeaders = [];
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
    this.documentHeaders = data ?? [];
    this.isShowingAlert = this.documentHeaders.length === 0;
    this._alertMessage = this.translateService.instant('dmsApp.document.home.notFound');
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
