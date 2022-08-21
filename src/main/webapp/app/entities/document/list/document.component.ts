import { Component, OnInit } from '@angular/core';
import { IDocumentInquiry } from '../document.model';
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
  documentHeaders?: IDocumentInquiry[];
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
    docTitle: [],
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
    this.loadPage();
  }

  trackId(index: number, item: IDocumentInquiry): number {
    return item.id!;
  }

  trackMetaDtaaByID(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  showFilters(): void {
    this.isShowingFilters = !this.isShowingFilters;
  }

  showResultArea(): void {
    this.isShowingResult = !this.isShowingResult;
  }

  loadAllSetup(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        console.log('Response Data :', res.body);
        this.metaDataHdrList = res.body;
      },
      error => {
        console.log('Response Failed : ', error);
      }
    );
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    this.documentInquiryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        // sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IDocumentInquiry[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IDocumentInquiry[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.documentHeaders = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
