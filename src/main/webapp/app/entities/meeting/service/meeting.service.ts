import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMeetingDelivery, IMeetingMessage } from '../meeting.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';

export type EntityResponseType = HttpResponse<IMeetingMessage>;
export type EntityArrayResponseType = HttpResponse<IMeetingDelivery[]>;

@Injectable({
  providedIn: 'root',
})
export class MeetingService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meeting');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  save(formData: FormData): Observable<HttpResponse<IReplyMessage>> {
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
}
