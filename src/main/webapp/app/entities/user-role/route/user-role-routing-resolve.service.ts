import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserRole, UserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';

@Injectable({ providedIn: 'root' })
export class UserRoleRoutingResolveService implements Resolve<IUserRole> {
  constructor(protected service: UserRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserRole> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userRole: HttpResponse<UserRole>) => {
          if (userRole.body) {
            return of(userRole.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserRole());
  }
}
