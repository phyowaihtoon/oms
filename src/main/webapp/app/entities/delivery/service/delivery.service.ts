import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IDeliveryMessage, IDocumentDelivery } from '../delivery.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { createRequestOption } from 'app/core/request/request-util';

export type EntityResponseType = HttpResponse<IDeliveryMessage>;
export type EntityArrayResponseType = HttpResponse<IDocumentDelivery[]>;

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
    return this.http.get<IDocumentDelivery[]>(`${this.resourceUrl}/received`, { params: options, observe: 'response' });
  }

  findAllSent(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentDelivery[]>(`${this.resourceUrl}/sent`, { params: options, observe: 'response' });
  }

  findAllDraft(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentDelivery[]>(`${this.resourceUrl}/draft`, { params: options, observe: 'response' });
  }
}
