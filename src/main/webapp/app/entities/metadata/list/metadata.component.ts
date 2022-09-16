import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaDataHeader, MetaDataInquiry } from '../metadata.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { MetaDataService } from '../service/metadata.service';
import { MetaDataDeleteDialogComponent } from '../delete/metadata-delete-dialog.component';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-metadata',
  templateUrl: './metadata.component.html',
  styleUrls: ['./metadata.component.scss'],
})
export class MetaDataComponent {
  metadatas?: IMetaDataHeader[];
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
    docTitle: [],
    createdDate: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected service: MetaDataService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  /* ngOnInit(): void {
    //this.handleNavigation();
  } */

  trackId(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  showFilters(): void {
    this.isShowingFilters = !this.isShowingFilters;
  }

  showResultArea(): void {
    this.isShowingResult = !this.isShowingResult;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    /* this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.service
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IMetaDataHeader[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      ); */

    this.isLoading = true;
    this.isShowingResult = true;
    this.metadatas = [];
    const pageToLoad: number = page ?? this.page ?? 1;
    const paginationReqParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      // sort: this.sort(),
    };
    const searchCriteria = {
      ...new MetaDataInquiry(),
      docTitle: this.searchForm.get('docTitle')!.value,
      createdDate: this.searchForm.get('createdDate')!.value ? this.searchForm.get('createdDate')!.value.format('DD-MM-YYYY') : '',
    };

    this.service.query(searchCriteria, paginationReqParams).subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  delete(metadata: IMetaDataHeader): void {
    const modalRef = this.modalService.open(MetaDataDeleteDialogComponent, { size: 'md', backdrop: 'static' });
    modalRef.componentInstance.metadata = metadata;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  clearFormData(): void {
    this.searchForm.reset();
    this.metadatas = [];
    this.isShowingResult = false;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  /*
  protected onSuccess(data: IMetaDataHeader[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/metadata'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.metadatas = data ?? [];
    this.ngbPaginationPage = this.page;
  }
  */

  protected onSuccess(data: IMetaDataHeader[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.metadatas = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
