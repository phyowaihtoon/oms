import { Component, OnInit } from '@angular/core';
import { DocumentHeader, IDocumentHeader, IDocumentInquiry } from '../document.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FormBuilder } from '@angular/forms';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { DocumentInquiryService } from '../service/document-inquiry.service';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';

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

  searchForm = this.fb.group({
    metaDataHdrID: [],
    repositoryURL: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected documentInquiryService: DocumentInquiryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected loadSetupService: LoadSetupService
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
      ...new DocumentHeader(),
      metaDataHeaderId: this.searchForm.get('metaDataHdrID')!.value,
      repositoryURL: this.searchForm.get('repositoryURL')!.value,
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
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
