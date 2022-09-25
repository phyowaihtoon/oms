import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMetaData, getMetaDataIdentifier } from '../meta-data.model';

export type EntityResponseType = HttpResponse<IMetaData>;
export type EntityArrayResponseType = HttpResponse<IMetaData[]>;

@Injectable({ providedIn: 'root' })
export class MetaDataService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-data');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(metaData: IMetaData): Observable<EntityResponseType> {
    return this.http.post<IMetaData>(this.resourceUrl, metaData, { observe: 'response' });
  }

  update(metaData: IMetaData): Observable<EntityResponseType> {
    return this.http.put<IMetaData>(`${this.resourceUrl}/${getMetaDataIdentifier(metaData) as number}`, metaData, { observe: 'response' });
  }

  partialUpdate(metaData: IMetaData): Observable<EntityResponseType> {
    return this.http.patch<IMetaData>(`${this.resourceUrl}/${getMetaDataIdentifier(metaData) as number}`, metaData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMetaData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMetaData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMetaDataToCollectionIfMissing(metaDataCollection: IMetaData[], ...metaDataToCheck: (IMetaData | null | undefined)[]): IMetaData[] {
    const metaData: IMetaData[] = metaDataToCheck.filter(isPresent);
    if (metaData.length > 0) {
      const metaDataCollectionIdentifiers = metaDataCollection.map(metaDataItem => getMetaDataIdentifier(metaDataItem)!);
      const metaDataToAdd = metaData.filter(metaDataItem => {
        const metaDataIdentifier = getMetaDataIdentifier(metaDataItem);
        if (metaDataIdentifier == null || metaDataCollectionIdentifiers.includes(metaDataIdentifier)) {
          return false;
        }
        metaDataCollectionIdentifiers.push(metaDataIdentifier);
        return true;
      });
      return [...metaDataToAdd, ...metaDataCollection];
    }
    return metaDataCollection;
  }
}
