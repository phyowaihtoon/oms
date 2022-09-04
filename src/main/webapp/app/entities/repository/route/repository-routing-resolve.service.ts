import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRepositoryHeader, RepositoryHeader } from '../repository.model';
import { RepositoryService } from '../service/repository.service';

@Injectable({ providedIn: 'root' })
export class RepositoryRoutingResolveService implements Resolve<IRepositoryHeader> {
  constructor(protected service: RepositoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRepositoryHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((repository: HttpResponse<RepositoryHeader>) => {
          console.log('Resolvess____' + JSON.stringify(repository.body));
          if (repository.body) {
            return of(repository.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RepositoryHeader());
  }
}