import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoleMenuMap, getRoleMenuMapIdentifier } from '../role-menu-map.model';

export type EntityResponseType = HttpResponse<IRoleMenuMap>;
export type EntityArrayResponseType = HttpResponse<IRoleMenuMap[]>;

@Injectable({ providedIn: 'root' })
export class RoleMenuMapService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/role-menu-maps');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(roleMenuMap: IRoleMenuMap): Observable<EntityResponseType> {
    return this.http.post<IRoleMenuMap>(this.resourceUrl, roleMenuMap, { observe: 'response' });
  }

  update(roleMenuMap: IRoleMenuMap): Observable<EntityResponseType> {
    return this.http.put<IRoleMenuMap>(`${this.resourceUrl}/${getRoleMenuMapIdentifier(roleMenuMap) as number}`, roleMenuMap, {
      observe: 'response',
    });
  }

  partialUpdate(roleMenuMap: IRoleMenuMap): Observable<EntityResponseType> {
    return this.http.patch<IRoleMenuMap>(`${this.resourceUrl}/${getRoleMenuMapIdentifier(roleMenuMap) as number}`, roleMenuMap, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoleMenuMap>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleMenuMap[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoleMenuMapToCollectionIfMissing(
    roleMenuMapCollection: IRoleMenuMap[],
    ...roleMenuMapsToCheck: (IRoleMenuMap | null | undefined)[]
  ): IRoleMenuMap[] {
    const roleMenuMaps: IRoleMenuMap[] = roleMenuMapsToCheck.filter(isPresent);
    if (roleMenuMaps.length > 0) {
      const roleMenuMapCollectionIdentifiers = roleMenuMapCollection.map(roleMenuMapItem => getRoleMenuMapIdentifier(roleMenuMapItem)!);
      const roleMenuMapsToAdd = roleMenuMaps.filter(roleMenuMapItem => {
        const roleMenuMapIdentifier = getRoleMenuMapIdentifier(roleMenuMapItem);
        if (roleMenuMapIdentifier == null || roleMenuMapCollectionIdentifiers.includes(roleMenuMapIdentifier)) {
          return false;
        }
        roleMenuMapCollectionIdentifiers.push(roleMenuMapIdentifier);
        return true;
      });
      return [...roleMenuMapsToAdd, ...roleMenuMapCollection];
    }
    return roleMenuMapCollection;
  }
}
