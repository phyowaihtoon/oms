import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMetaDataHeader, getMetadataIdentifier } from '../metadata.model';

export type EntityResponseType = HttpResponse<IMetaDataHeader>;
export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;

@Injectable({ providedIn: 'root' })
export class MetaDataService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-data');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(metadata: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.post<IMetaDataHeader>(this.resourceUrl, metadata, { observe: 'response' });
  }

  update(metadata: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.put<IMetaDataHeader>(`${this.resourceUrl}/${getMetadataIdentifier(metadata) as number}`, metadata, {
      observe: 'response',
    });
  }

  partialUpdate(metadata: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.patch<IMetaDataHeader>(`${this.resourceUrl}/${getMetadataIdentifier(metadata) as number}`, metadata, {
      observe: 'response',
    });
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
    ...categoriesToCheck: (IMetaDataHeader | null | undefined)[]
  ): IMetaDataHeader[] {
    const metaDataHeader: IMetaDataHeader[] = categoriesToCheck.filter(isPresent);
    if (metaDataHeader.length > 0) {
      const collectionIdentifiers = metaDataHeaderCollection.map(categoryItem => getMetadataIdentifier(categoryItem)!);
      const categoriesToAdd = metaDataHeader.filter(metaDataHeaderItem => {
        const metaDataHeaderIdentifier = getMetadataIdentifier(metaDataHeaderItem);
        if (metaDataHeaderIdentifier == null || collectionIdentifiers.includes(metaDataHeaderIdentifier)) {
          return false;
        }
        collectionIdentifiers.push(metaDataHeaderIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...metaDataHeaderCollection];
    }
    return metaDataHeaderCollection;
  }
}
