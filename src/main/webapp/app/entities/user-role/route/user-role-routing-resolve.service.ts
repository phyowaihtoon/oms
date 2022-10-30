import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { UserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';
import { IHeaderDetailsMessage } from 'app/entities/util/reply-message.model';

@Injectable({ providedIn: 'root' })
export class UserRoleRoutingResolveService implements Resolve<IHeaderDetailsMessage> {
  constructor(protected service: UserRoleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHeaderDetailsMessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userRole: HttpResponse<IHeaderDetailsMessage>) => {
          if (userRole.body) {
            return of(userRole.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    const message: IHeaderDetailsMessage = { header: new UserRole(), details: [] };
    return of(message);
  }
}
