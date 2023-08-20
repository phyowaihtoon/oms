import { Component, OnInit, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { HttpResponse } from '@angular/common/http';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { IMenuGroupMessage } from 'app/entities/user-role/user-role.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { interval } from 'rxjs';
import { IUserNotification } from 'app/login/userauthority.model';

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
  _notiCount: number = 0;
  _notificationList: IUserNotification[] = [];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private $sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private userAuthorityService: UserAuthorityService,
    private ngZone: NgZone,
    private router: Router,
    protected modalService: NgbModal
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

    const interval$ = interval(5000);
    interval$.subscribe(() => this.getUserNotiCount());
  }

  showCodeInfo(): void {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
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

  getUserNotiCount(): void {
    console.log('Trying to get Notification Count :');
    if (this.isAuthenticated()) {
      this.userAuthorityService.getUserNotiCount().subscribe(
        (res: HttpResponse<IUserNotification[]>) => {
          if (res.body) {
            this._notificationList = res.body;
            this._notiCount = this._notificationList.length;
            console.log('Notification Count :', this._notificationList.length);
          }
        },
        error => {
          console.log('Loading Notification Count Failed : ', error);
        }
      );
    }
  }
}
