import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DashboardTemplate, IDashboardTemplate } from 'app/services/dashboard-template.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;

  template1 = this.createTemplate('CARD001', 'Information');
  template2 = this.createTemplate('CARD002', 'Document Mapping Status');
  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    // this.loadPage();
  }

  /*
  loadPage(): void {
    this.templates?.push(this.createTemplate('CARD001','Document Dashboard'));
  }
  */

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
