import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentHeader, getDocumentHeaderIdentifier } from '../document-header.model';

export type EntityResponseType = HttpResponse<IDocumentHeader>;
export type EntityArrayResponseType = HttpResponse<IDocumentHeader[]>;

@Injectable({ providedIn: 'root' })
export class DocumentHeaderService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/document-headers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.post<IDocumentHeader>(this.resourceUrl, documentHeader, { observe: 'response' });
  }

  update(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.put<IDocumentHeader>(`${this.resourceUrl}/${getDocumentHeaderIdentifier(documentHeader) as number}`, documentHeader, {
      observe: 'response',
    });
  }

  partialUpdate(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.patch<IDocumentHeader>(
      `${this.resourceUrl}/${getDocumentHeaderIdentifier(documentHeader) as number}`,
      documentHeader,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentHeaderToCollectionIfMissing(
    documentHeaderCollection: IDocumentHeader[],
    ...documentHeadersToCheck: (IDocumentHeader | null | undefined)[]
  ): IDocumentHeader[] {
    const documentHeaders: IDocumentHeader[] = documentHeadersToCheck.filter(isPresent);
    if (documentHeaders.length > 0) {
      const documentHeaderCollectionIdentifiers = documentHeaderCollection.map(
        documentHeaderItem => getDocumentHeaderIdentifier(documentHeaderItem)!
      );
      const documentHeadersToAdd = documentHeaders.filter(documentHeaderItem => {
        const documentHeaderIdentifier = getDocumentHeaderIdentifier(documentHeaderItem);
        if (documentHeaderIdentifier == null || documentHeaderCollectionIdentifiers.includes(documentHeaderIdentifier)) {
          return false;
        }
        documentHeaderCollectionIdentifiers.push(documentHeaderIdentifier);
        return true;
      });
      return [...documentHeadersToAdd, ...documentHeaderCollection];
    }
    return documentHeaderCollection;
  }
}
