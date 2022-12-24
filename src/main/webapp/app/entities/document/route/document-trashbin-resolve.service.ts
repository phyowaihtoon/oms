import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { DocumentHeader, IDocumentHeader } from '../document.model';
import { DocumentInquiryService } from '../service/document-inquiry.service';

@Injectable({
  providedIn: 'root',
})
export class DocumentTrashbinResolveService implements Resolve<IDocumentHeader> {
  constructor(protected service: DocumentInquiryService, protected router: Router) {}
  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentHeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.getDeletedDocumentsByHeaderId(id).pipe(
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
