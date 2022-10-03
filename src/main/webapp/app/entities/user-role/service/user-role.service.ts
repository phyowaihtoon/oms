import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserRole, getUserRoleIdentifier } from '../user-role.model';

export type EntityResponseType = HttpResponse<IUserRole>;
export type EntityArrayResponseType = HttpResponse<IUserRole[]>;

@Injectable({ providedIn: 'root' })
export class UserRoleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/user-roles');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(userRole: IUserRole): Observable<EntityResponseType> {
    return this.http.post<IUserRole>(this.resourceUrl, userRole, { observe: 'response' });
  }

  update(userRole: IUserRole): Observable<EntityResponseType> {
    return this.http.put<IUserRole>(`${this.resourceUrl}/${getUserRoleIdentifier(userRole) as number}`, userRole, { observe: 'response' });
  }

  partialUpdate(userRole: IUserRole): Observable<EntityResponseType> {
    return this.http.patch<IUserRole>(`${this.resourceUrl}/${getUserRoleIdentifier(userRole) as number}`, userRole, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
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
}
