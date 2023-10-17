import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IDeliveryMessage, IDocumentDelivery } from '../delivery.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

export type EntityResponseType = HttpResponse<IDeliveryMessage>;
export type EntityArrayResponseType = HttpResponse<IDocumentDelivery[]>;
export type BlobType = HttpResponse<Blob>;

@Injectable({
  providedIn: 'root',
})
export class DeliveryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  save(formData: FormData): Observable<HttpResponse<IReplyMessage>> {
    console.log('Delivery URL', this.resourceUrl);
    return this.http.post<IReplyMessage>(this.resourceUrl, formData, { observe: 'response' });
  }

  update(formData: FormData, id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/${id}`, formData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllReceived(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentDelivery[]>(`${this.resourceUrl}/received`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllSent(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentDelivery[]>(`${this.resourceUrl}/sent`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllDraft(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentDelivery[]>(`${this.resourceUrl}/draft`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getPreviewData(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/preview/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  downloadFile(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/download/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentDelivery: IDocumentDelivery) => {
        (documentDelivery.sentDate = documentDelivery.sentDate ? dayjs(documentDelivery.sentDate) : undefined),
          (documentDelivery.createdDate = documentDelivery.createdDate ? dayjs(documentDelivery.createdDate) : undefined);
      });
    }
    return res;
  }
}
