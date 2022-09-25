import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaData, MetaData } from '../meta-data.model';
import { MetaDataService } from '../service/meta-data.service';

@Injectable({ providedIn: 'root' })
export class MetaDataRoutingResolveService implements Resolve<IMetaData> {
  constructor(protected service: MetaDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMetaData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((metaData: HttpResponse<MetaData>) => {
          if (metaData.body) {
            return of(metaData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MetaData());
  }
}
