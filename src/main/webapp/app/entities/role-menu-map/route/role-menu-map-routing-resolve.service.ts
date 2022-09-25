import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoleMenuMap, RoleMenuMap } from '../role-menu-map.model';
import { RoleMenuMapService } from '../service/role-menu-map.service';

@Injectable({ providedIn: 'root' })
export class RoleMenuMapRoutingResolveService implements Resolve<IRoleMenuMap> {
  constructor(protected service: RoleMenuMapService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoleMenuMap> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roleMenuMap: HttpResponse<RoleMenuMap>) => {
          if (roleMenuMap.body) {
            return of(roleMenuMap.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoleMenuMap());
  }
}
