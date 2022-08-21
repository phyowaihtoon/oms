import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaDataHeader, MetaDataHeader } from '../metadata.model';
import { MetaDataService } from '../service/metadata.service';

@Injectable({ providedIn: 'root' })
export class MetaDataRoutingResolveService implements Resolve<IMetaDataHeader> {
  constructor(protected service: MetaDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMetaDataHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((metadata: HttpResponse<MetaDataHeader>) => {
          if (metadata.body) {
            return of(metadata.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MetaDataHeader());
  }
}
