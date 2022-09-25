import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentHeader, DocumentHeader } from '../document-header.model';
import { DocumentHeaderService } from '../service/document-header.service';

@Injectable({ providedIn: 'root' })
export class DocumentHeaderRoutingResolveService implements Resolve<IDocumentHeader> {
  constructor(protected service: DocumentHeaderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentHeader: HttpResponse<DocumentHeader>) => {
          if (documentHeader.body) {
            return of(documentHeader.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentHeader());
  }
}
