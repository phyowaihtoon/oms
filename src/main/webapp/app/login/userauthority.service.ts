import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { SessionStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IUserAuthority, IUserNotification } from './userauthority.model';

export type EntityResponseType = HttpResponse<IUserAuthority>;
export type NotificationResponseType = HttpResponse<IUserNotification[]>;

@Injectable({ providedIn: 'root' })
export class UserAuthorityService {
  private userAuthorityAPI = this.applicationConfigService.getEndpointFor('api/userauthority');
  private userNotiCountAPI = this.applicationConfigService.getEndpointFor('api/noticount');

  constructor(
    protected http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private $sessionStorage: SessionStorageService
  ) {}

  getUserNotiCount(): Observable<NotificationResponseType> {
    console.log(this.userNotiCountAPI," User Noticount URL");
    return this.http.get<IUserNotification[]>(this.userNotiCountAPI, { observe: 'response' });
  }

  getUserAuthority(): Observable<void> {
    return this.http
      .get<IUserAuthority>(this.userAuthorityAPI, { observe: 'response' })
      .pipe(
        map(response => {
          if (response.body) {
            this.storeUserAuthority(response.body);
          }
        })
      );
  }

  retrieveUserAuthority(): IUserAuthority | null {
    const userAuthority: IUserAuthority | null = this.$sessionStorage.retrieve('UserAuthority');
    return userAuthority;
  }

  clearUserAuthority(): void {
    this.$sessionStorage.clear('UserAuthority');
  }

  private storeUserAuthority(userAuthority: IUserAuthority): void {
    this.$sessionStorage.store('UserAuthority', userAuthority);
  }
}
