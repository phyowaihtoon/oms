import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserRole } from '../user-role.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { UserRoleService } from '../service/user-role.service';
import { UserRoleDeleteDialogComponent } from '../delete/user-role-delete-dialog.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { IUserAuthority } from 'app/login/userauthority.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-user-role',
  templateUrl: './user-role.component.html',
})
export class UserRoleComponent implements OnInit {
  userRoles?: IUserRole[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;
  _isSystemUser: boolean = false;

  constructor(
    private accountService: AccountService,
    protected userRoleService: UserRoleService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.userRoleService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IUserRole[]>) => {
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

  trackId(index: number, item: IUserRole): number {
    return item.id!;
  }

  getRoleTypeDesc(value?: number): string {
    if (value === 1) {
      return 'YES';
    }
    return 'NO';
  }

  delete(userRole: IUserRole): void {
    if (userRole.id) {
      this.userRoleService.checkDependency(userRole.id).subscribe((res: HttpResponse<IReplyMessage>) => {
        if (res.body) {
          const replyMessage = res.body;

          // Dependency exists for this user role
          if (replyMessage.code === ResponseCode.WARNING) {
            const replyCode = replyMessage.code;
            const replyMsg = replyMessage.message;
            this.showAlertMessage(replyCode, replyMsg);
          }

          // No Dependency exists for this user role.
          if (replyMessage.code === ResponseCode.SUCCESS) {
            const modalRef = this.modalService.open(UserRoleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
            modalRef.componentInstance.userRole = userRole;
            modalRef.closed.subscribe(reason => {
              if (reason === 'deleted') {
                this.loadPage();
              }
            });
          }
        }
      });
    }
  }

  protected showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
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

  protected onSuccess(data: IUserRole[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/user-role'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.userRoles = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
