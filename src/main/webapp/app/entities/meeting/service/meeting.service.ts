import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMeetingDelivery, IMeetingMessage } from '../meeting.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { createRequestOption } from 'app/core/request/request-util';

export type EntityResponseType = HttpResponse<IMeetingMessage>;
export type EntityArrayResponseType = HttpResponse<IMeetingDelivery[]>;
export type BlobType = HttpResponse<Blob>;

@Injectable({
  providedIn: 'root',
})
export class MeetingService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meeting');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  save(formData: FormData, message: IMeetingMessage): Observable<HttpResponse<IReplyMessage>> {
    if (message.meetingDelivery) {
      const meetingDelivery = this.convertDateFromClient(message.meetingDelivery);
      message.meetingDelivery = meetingDelivery;
      formData.append('meeting', JSON.stringify(message));
    }
    return this.http.post<IReplyMessage>(this.resourceUrl, formData, { observe: 'response' });
  }

  update(formData: FormData, id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/${id}`, formData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeetingMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllReceived(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeetingDelivery[]>(`${this.resourceUrl}/received`, { params: options, observe: 'response' });
  }

  findAllSent(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeetingDelivery[]>(`${this.resourceUrl}/sent`, { params: options, observe: 'response' });
  }

  findAllDraft(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeetingDelivery[]>(`${this.resourceUrl}/draft`, { params: options, observe: 'response' });
  }

  getScheduledMeetingList(): Observable<EntityArrayResponseType> {
    return this.http.get<IMeetingDelivery[]>(`${this.resourceUrl}/scheduled`, { observe: 'response' });
  }

  getPreviewData(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/preview/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  downloadFile(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/download/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  protected convertDateFromClient(meetingDelivery: IMeetingDelivery): IMeetingDelivery {
    return Object.assign({}, meetingDelivery, {
      startDate: meetingDelivery.startDate?.isValid() ? meetingDelivery.startDate.format('YYYY-MM-DDTHH:mm:ssZ') : undefined,
      endDate: meetingDelivery.endDate?.isValid() ? meetingDelivery.endDate.format('YYYY-MM-DDTHH:mm:ssZ') : undefined,
    });
  }
}
