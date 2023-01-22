import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DashboardTemplate, IDashboardTemplate } from 'app/services/dashboard-template.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { UserAuthorityService } from 'app/login/userauthority.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  _userAuthority?: IUserAuthority | null;

  templates?: IDashboardTemplate[];
  constructor(
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected userAuthorityService: UserAuthorityService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    if (this.isAuthenticated()) {
      this._userAuthority = this.userAuthorityService.retrieveUserAuthority();
      this.templates = this._userAuthority?.dashboardTemplates;
    }
  }

  createTemplate(cardId: string, cardName: string): IDashboardTemplate {
    return {
      ...new DashboardTemplate(),
      id: undefined,
      cardId: cardId || '',
      cardName: cardName || '',
    };
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
