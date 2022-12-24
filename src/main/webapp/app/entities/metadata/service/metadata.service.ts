import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMetaDataHeader, getMetadataIdentifier, IMetaDataInquiry } from '../metadata.model';

import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { IReplyMessage } from 'app/entities/util/reply-message.model';

export type EntityResponseType = HttpResponse<IMetaDataHeader>;
export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;

@Injectable({ providedIn: 'root' })
export class MetaDataService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-data');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(metadata: IMetaDataHeader): Observable<HttpResponse<IReplyMessage>> {
    return this.http.post<IReplyMessage>(this.resourceUrl + '/save', metadata, { observe: 'response' });
  }

  update(metadata: IMetaDataHeader): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/${getMetadataIdentifier(metadata) as number}`, metadata, {
      observe: 'response',
    });
  }

  partialUpdate(metadata: IMetaDataHeader): Observable<EntityResponseType> {
    return this.http.patch<IMetaDataHeader>(`${this.resourceUrl}/${getMetadataIdentifier(metadata) as number}`, metadata, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMetaDataHeader>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(criteriaData: IMetaDataInquiry, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IMetaDataHeader[]>(this.resourceUrl + '/search', criteriaData, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllMetaDataInTrashBin(criteriaData: IMetaDataInquiry, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IMetaDataHeader[]>(this.resourceUrl + '/trashbin', criteriaData, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentHeader: IMetaDataHeader) => {
        documentHeader.createdDate = documentHeader.createdDate ? dayjs(documentHeader.createdDate) : undefined;
      });
    }
    return res;
  }

  delete(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.delete<IReplyMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
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

  restoreMetaData(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(
      `${this.resourceUrl}/restore/${id}`,
      {},
      {
        observe: 'response',
      }
    );
  }

  deleteField(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.get<IReplyMessage>(`${this.resourceUrl}/deleteFieldById/${id}`, { observe: 'response' });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
    }
    return res;
  }
}
