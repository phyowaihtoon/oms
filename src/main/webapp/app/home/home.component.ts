import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DashboardTemplate, IDashboardTemplate } from 'app/services/dashboard-template.model';
import { HttpResponse } from '@angular/common/http';
import { DashboardService } from 'app/services/dashboard-service';
import { SchowChartService } from 'app/chart/showchart.service';
import { IUserAuthority } from 'app/login/userauthority.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { UserAuthorityService } from 'app/login/userauthority.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;

  //_activeMenuItem?: IMenuItem;
  templates?: IDashboardTemplate[];
  constructor(
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private dashboardService: SchowChartService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.loadPage();
  }

  loadPage(): void {
    this.dashboardService.getAllTemplate().subscribe((res: HttpResponse<IDashboardTemplate[]>) => {
      if (res.body) {
        this.templates = res.body;
      }
    });
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

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
