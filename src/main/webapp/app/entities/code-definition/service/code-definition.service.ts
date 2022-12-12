import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { CodeDefinition, ICodeDefinition } from '../code-definition.model';

export type PartialUpdateCodeDefinition = Partial<ICodeDefinition> & Pick<ICodeDefinition, 'id'>;

export type EntityResponseType = HttpResponse<ICodeDefinition>;
export type EntityArrayResponseType = HttpResponse<ICodeDefinition[]>;

@Injectable({ providedIn: 'root' })
export class CodeDefinitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/code-definitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(codeDefinition: CodeDefinition): Observable<EntityResponseType> {
    return this.http.post<ICodeDefinition>(this.resourceUrl, codeDefinition, { observe: 'response' });
  }

  update(codeDefinition: ICodeDefinition): Observable<EntityResponseType> {
    return this.http.put<ICodeDefinition>(`${this.resourceUrl}/${this.getCodeDefinitionIdentifier(codeDefinition)}`, codeDefinition, {
      observe: 'response',
    });
  }

  partialUpdate(codeDefinition: PartialUpdateCodeDefinition): Observable<EntityResponseType> {
    return this.http.patch<ICodeDefinition>(`${this.resourceUrl}/${this.getCodeDefinitionIdentifier(codeDefinition)}`, codeDefinition, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodeDefinition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodeDefinition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCodeDefinitionIdentifier(codeDefinition: Pick<ICodeDefinition, 'id'>): number {
    return codeDefinition.id;
  }

  compareCodeDefinition(o1: Pick<ICodeDefinition, 'id'> | null, o2: Pick<ICodeDefinition, 'id'> | null): boolean {
    return o1 && o2 ? this.getCodeDefinitionIdentifier(o1) === this.getCodeDefinitionIdentifier(o2) : o1 === o2;
  }

  addCodeDefinitionToCollectionIfMissing<Type extends Pick<ICodeDefinition, 'id'>>(
    codeDefinitionCollection: Type[],
    ...codeDefinitionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const codeDefinitions: Type[] = codeDefinitionsToCheck.filter(isPresent);
    if (codeDefinitions.length > 0) {
      const codeDefinitionCollectionIdentifiers = codeDefinitionCollection.map(
        codeDefinitionItem => this.getCodeDefinitionIdentifier(codeDefinitionItem)!
      );
      const codeDefinitionsToAdd = codeDefinitions.filter(codeDefinitionItem => {
        const codeDefinitionIdentifier = this.getCodeDefinitionIdentifier(codeDefinitionItem);
        if (codeDefinitionCollectionIdentifiers.includes(codeDefinitionIdentifier)) {
          return false;
        }
        codeDefinitionCollectionIdentifiers.push(codeDefinitionIdentifier);
        return true;
      });
      return [...codeDefinitionsToAdd, ...codeDefinitionCollection];
    }
    return codeDefinitionCollection;
  }
}
