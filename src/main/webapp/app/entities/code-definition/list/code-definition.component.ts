import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICodeDefinition } from '../code-definition.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CodeDefinitionService } from '../service/code-definition.service';
import { CodeDefinitionDeleteDialogComponent } from '../delete/code-definition-delete-dialog.component';
import { IUserAuthority } from 'app/login/userauthority.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-code-definition',
  templateUrl: './code-definition.component.html',
})
export class CodeDefinitionComponent implements OnInit {
  codeDefinitions?: ICodeDefinition[];
  isLoading = false;
  predicate = 'id';
  ascending = true;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page?: number;
  ngbPaginationPage = 1;

  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;
  _isSystemUser: boolean = false;

  constructor(
    private accountService: AccountService,
    protected codeDefinitionService: CodeDefinitionService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(pageNum?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = pageNum ?? this.page ?? 1;

    this.codeDefinitionService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ICodeDefinition[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();

    this.activatedRoute.data.subscribe(({ userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;
    });

    this._isSystemUser = this.accountService.hasAnyAuthority('SYSTEM_USER');
  }

  trackId(index: number, item: ICodeDefinition): number | undefined {
    return item.id;
  }

  delete(codeDefinition: ICodeDefinition): void {
    const modalRef = this.modalService.open(CodeDefinitionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.codeDefinition = codeDefinition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
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

  protected onSuccess(data: ICodeDefinition[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/code-definition'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.codeDefinitions = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
