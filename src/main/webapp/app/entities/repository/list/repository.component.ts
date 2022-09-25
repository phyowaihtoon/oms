import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRepositoryHeader, RepositoryInquiry } from '../repository.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RepositoryService } from '../service/repository.service';
import { RepositoryDeleteDialogComponent } from '../delete/repository-delete-dialog.component';
import { FormBuilder } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-repository',
  templateUrl: './repository.component.html',
  styleUrls: ['./repository.component.scss'],
})
export class RepositoryComponent {
  repositorys?: IRepositoryHeader[];
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
    repositoryName: [],
    createdDate: [],
  });

  constructor(
    protected fb: FormBuilder,
    protected service: RepositoryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected translateService: TranslateService
  ) {}

  /* ngOnInit(): void {
    this.handleNavigation();
  } */

  trackId(index: number, item: IRepositoryHeader): number {
    return item.id!;
  }

  showFilters(): void {
    this.isShowingFilters = !this.isShowingFilters;
  }

  showResultArea(): void {
    this.isShowingResult = !this.isShowingResult;
  }

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  loadRepositoryPath(repoDetails: any): string {
    let str = '';
    repoDetails.forEach((data: any) => {
      if (str === '') {
        str = String(data.folderName);
      } else {
        str = str + '//' + String(data.folderName);
      }
    });
    return str;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    this.isShowingResult = true;
    this.repositorys = [];
    const pageToLoad: number = page ?? this.page ?? 1;
    const paginationReqParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      // sort: this.sort(),
    };
    const searchCriteria = {
      ...new RepositoryInquiry(),
      repositoryName: this.searchForm.get('repositoryName')!.value,
      createdDate: this.searchForm.get('createdDate')!.value ? this.searchForm.get('createdDate')!.value.format('DD-MM-YYYY') : '',
    };

    this.service.query(searchCriteria, paginationReqParams).subscribe(
      (res: HttpResponse<IRepositoryHeader[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  delete(repository: IRepositoryHeader): void {
    const modalRef = this.modalService.open(RepositoryDeleteDialogComponent, { size: 'md', backdrop: 'static' });
    modalRef.componentInstance.repository = repository;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  clearFormData(): void {
    this.searchForm.reset();
    this.repositorys = [];
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

  protected onSuccess(data: IRepositoryHeader[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.repositorys = data ?? [];
    this.isShowingAlert = this.repositorys.length === 0;
    this._alertMessage = this.translateService.instant('dmsApp.repository.home.notFound');
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
