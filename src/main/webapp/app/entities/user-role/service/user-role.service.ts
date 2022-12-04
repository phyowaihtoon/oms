import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserRole, getUserRoleIdentifier, IRoleMenuAccess } from '../user-role.model';
import { IHeaderDetailsMessage, IReplyMessage } from 'app/entities/util/reply-message.model';

export type EntityResponseType = HttpResponse<IUserRole>;
export type EntityArrayResponseType = HttpResponse<IUserRole[]>;
export type MenuAccessResponseType = HttpResponse<IRoleMenuAccess[]>;
export type HeaderDetailsResponseType = HttpResponse<IHeaderDetailsMessage>;
export type ReplyMessageResponseType = HttpResponse<IReplyMessage>;

@Injectable({ providedIn: 'root' })
export class UserRoleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/user-roles');
  public menuAccessResourceUrl = this.applicationConfigService.getEndpointFor('api/menu-item');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(headerDetailsMessage: IHeaderDetailsMessage): Observable<HeaderDetailsResponseType> {
    return this.http.post<IHeaderDetailsMessage>(this.resourceUrl, headerDetailsMessage, { observe: 'response' });
  }

  update(headerDetailsMessage: IHeaderDetailsMessage): Observable<HeaderDetailsResponseType> {
    return this.http.put<IHeaderDetailsMessage>(
      `${this.resourceUrl}/${getUserRoleIdentifier(headerDetailsMessage.header) as number}`,
      headerDetailsMessage,
      { observe: 'response' }
    );
  }

  partialUpdate(userRole: IUserRole): Observable<EntityResponseType> {
    return this.http.patch<IUserRole>(`${this.resourceUrl}/${getUserRoleIdentifier(userRole) as number}`, userRole, {
      observe: 'response',
    });
  }

  find(id: number): Observable<HeaderDetailsResponseType> {
    return this.http.get<IHeaderDetailsMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserRoleToCollectionIfMissing(userRoleCollection: IUserRole[], ...userRolesToCheck: (IUserRole | null | undefined)[]): IUserRole[] {
    const userRoles: IUserRole[] = userRolesToCheck.filter(isPresent);
    if (userRoles.length > 0) {
      const userRoleCollectionIdentifiers = userRoleCollection.map(userRoleItem => getUserRoleIdentifier(userRoleItem)!);
      const userRolesToAdd = userRoles.filter(userRoleItem => {
        const userRoleIdentifier = getUserRoleIdentifier(userRoleItem);
        if (userRoleIdentifier == null || userRoleCollectionIdentifiers.includes(userRoleIdentifier)) {
          return false;
        }
        userRoleCollectionIdentifiers.push(userRoleIdentifier);
        return true;
      });
      return [...userRolesToAdd, ...userRoleCollection];
    }
    return userRoleCollection;
  }

  getAllMenuItems(): Observable<MenuAccessResponseType> {
    return this.http.get<IRoleMenuAccess[]>(this.menuAccessResourceUrl, { observe: 'response' });
  }

  checkDependency(id: number): Observable<ReplyMessageResponseType> {
    return this.http.get<IReplyMessage>(`${this.resourceUrl}/check/${id}`, { observe: 'response' });
  }
}
