import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodeDefinition } from '../code-definition.model';
import { CodeDefinitionService } from '../service/code-definition.service';

@Injectable({ providedIn: 'root' })
export class CodeDefinitionRoutingResolveService implements Resolve<ICodeDefinition | null> {
  constructor(protected service: CodeDefinitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICodeDefinition | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((codeDefinition: HttpResponse<ICodeDefinition>) => {
          if (codeDefinition.body) {
            return of(codeDefinition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
