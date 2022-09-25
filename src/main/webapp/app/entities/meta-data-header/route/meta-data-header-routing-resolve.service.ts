import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaDataHeader, MetaDataHeader } from '../meta-data-header.model';
import { MetaDataHeaderService } from '../service/meta-data-header.service';

@Injectable({ providedIn: 'root' })
export class MetaDataHeaderRoutingResolveService implements Resolve<IMetaDataHeader> {
  constructor(protected service: MetaDataHeaderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMetaDataHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((metaDataHeader: HttpResponse<MetaDataHeader>) => {
          if (metaDataHeader.body) {
            return of(metaDataHeader.body);
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
