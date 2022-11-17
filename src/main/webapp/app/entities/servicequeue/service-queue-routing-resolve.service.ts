import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { DocumentHeader, IDocumentHeader } from '../document/document.model';
import { DocumentInquiryService } from '../document/service/document-inquiry.service';

@Injectable({
  providedIn: 'root',
})
export class ServiceQueueRoutingResolveService implements Resolve<IDocumentHeader> {
  constructor(protected service: DocumentInquiryService, protected router: Router) {}
  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.getDocumentsById(id).pipe(
        mergeMap((response: HttpResponse<DocumentHeader>) => {
          if (response.body) {
            return of(response.body);
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
