import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { IMenuGroupMessage } from 'app/entities/user-role/user-role.model';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private $sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private userAuthorityService: UserAuthorityService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    if (!this.isAuthenticated()) {
      this.router.navigate(['/login']);
    }
  }

  changeLanguage(languageKey: string): void {
    this.$sessionStorage.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  getApplicationMenus(): IMenuGroupMessage[] {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    if (userAuthority?.menuGroups) {
      return userAuthority.menuGroups.filter(value => value.groupCode !== 'SYSMG');
    }
    return [];
  }

  getSystemMenus(): IMenuGroupMessage[] {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    if (userAuthority?.menuGroups && this.accountService.hasAnyAuthority('APPLICATION_USER')) {
      return userAuthority.menuGroups.filter(value => value.groupCode === 'SYSMG');
    }
    return [];
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.userAuthorityService.clearUserAuthority();
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }
}
