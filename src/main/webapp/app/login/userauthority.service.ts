import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { SessionStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IUserAuthority } from './userauthority.model';

export type EntityResponseType = HttpResponse<IUserAuthority>;

@Injectable({ providedIn: 'root' })
export class UserAuthorityService {
  private userAuthorityAPI = this.applicationConfigService.getEndpointFor('api/userauthority');

  constructor(
    protected http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private $sessionStorage: SessionStorageService
  ) {}

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

  private storeUserAuthority(userAuthority: IUserAuthority): void {
    this.$sessionStorage.store('UserAuthority', userAuthority);
  }
}
