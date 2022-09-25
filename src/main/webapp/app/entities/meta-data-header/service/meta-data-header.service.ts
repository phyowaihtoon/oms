import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMetaDataHeader, getMetaDataHeaderIdentifier } from '../meta-data-header.model';

export type EntityResponseType = HttpResponse<IMetaDataHeader>;
export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;

@Injectable({ providedIn: 'root' })
export class MetaDataHeaderService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-data-headers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(metaDataHeader: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.post<IMetaDataHeader>(this.resourceUrl, metaDataHeader, { observe: 'response' });
  }

  update(metaDataHeader: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.put<IMetaDataHeader>(`${this.resourceUrl}/${getMetaDataHeaderIdentifier(metaDataHeader) as number}`, metaDataHeader, {
      observe: 'response',
    });
  }

  partialUpdate(metaDataHeader: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.patch<IMetaDataHeader>(
      `${this.resourceUrl}/${getMetaDataHeaderIdentifier(metaDataHeader) as number}`,
      metaDataHeader,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMetaDataHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMetaDataHeader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMetaDataHeaderToCollectionIfMissing(
    metaDataHeaderCollection: IMetaDataHeader[],
    ...metaDataHeadersToCheck: (IMetaDataHeader | null | undefined)[]
  ): IMetaDataHeader[] {
    const metaDataHeaders: IMetaDataHeader[] = metaDataHeadersToCheck.filter(isPresent);
    if (metaDataHeaders.length > 0) {
      const metaDataHeaderCollectionIdentifiers = metaDataHeaderCollection.map(
        metaDataHeaderItem => getMetaDataHeaderIdentifier(metaDataHeaderItem)!
      );
      const metaDataHeadersToAdd = metaDataHeaders.filter(metaDataHeaderItem => {
        const metaDataHeaderIdentifier = getMetaDataHeaderIdentifier(metaDataHeaderItem);
        if (metaDataHeaderIdentifier == null || metaDataHeaderCollectionIdentifiers.includes(metaDataHeaderIdentifier)) {
          return false;
        }
        metaDataHeaderCollectionIdentifiers.push(metaDataHeaderIdentifier);
        return true;
      });
      return [...metaDataHeadersToAdd, ...metaDataHeaderCollection];
    }
    return metaDataHeaderCollection;
  }
}
