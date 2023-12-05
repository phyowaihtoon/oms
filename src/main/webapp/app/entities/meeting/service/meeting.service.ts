import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMeetingDelivery, IMeetingMessage } from '../meeting.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { ISearchCriteria } from 'app/entities/util/criteria.model';
import { SessionStorageService } from 'ngx-webstorage';

export type EntityResponseType = HttpResponse<IMeetingMessage>;
export type EntityArrayResponseType = HttpResponse<IMeetingDelivery[]>;
export type BlobType = HttpResponse<Blob>;

@Injectable({
  providedIn: 'root',
})
export class MeetingService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meeting');
  private previousState: string = '';
  constructor(
    protected http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private $sessionStorage: SessionStorageService
  ) {}

  getSearchCriteria(key: string): ISearchCriteria | null {
    const searchedCriteria: ISearchCriteria | null = this.$sessionStorage.retrieve(key);
    if (this.previousState === key) {
      return searchedCriteria;
    } else {
      this.clearSearchCriteria(key);
      return null;
    }
  }

  clearSearchCriteria(key: string): void {
    this.$sessionStorage.clear(key);
  }

  storeSearchCriteria(key: string, searchCriteria?: ISearchCriteria): void {
    this.$sessionStorage.store(key, searchCriteria);
  }

  setPreviousState(preState: string): void {
    this.previousState = preState;
  }

  clearPreviousState(): void {
    this.previousState = '';
  }

  save(formData: FormData, message: IMeetingMessage): Observable<HttpResponse<IReplyMessage>> {
    if (message.meetingDelivery) {
      const meetingDelivery = this.convertDateFromClient(message.meetingDelivery);
      message.meetingDelivery = meetingDelivery;
      formData.append('meeting', JSON.stringify(message));
    }
    return this.http.post<IReplyMessage>(this.resourceUrl, formData, { observe: 'response' });
  }

  update(formData: FormData, message: IMeetingMessage, id: number): Observable<HttpResponse<IReplyMessage>> {
    if (message.meetingDelivery) {
      const meetingDelivery = this.convertDateFromClient(message.meetingDelivery);
      message.meetingDelivery = meetingDelivery;
      formData.append('meeting', JSON.stringify(message));
    }
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/${id}`, formData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeetingMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findAllReceived(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeetingDelivery[]>(`${this.resourceUrl}/received`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllSent(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeetingDelivery[]>(`${this.resourceUrl}/sent`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllDraft(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeetingDelivery[]>(`${this.resourceUrl}/draft`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getScheduledMeetingList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IMeetingDelivery[]>(`${this.resourceUrl}/scheduled`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getPreviewData(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/preview/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  downloadFile(attachmentId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/download/${attachmentId}`, { observe: 'response', responseType: 'blob' });
  }

  markAsRead(meetingId: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/read/${meetingId}`, '', {
      observe: 'response',
    });
  }

  markAsUnRead(meetingId: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/unread/${meetingId}`, '', {
      observe: 'response',
    });
  }

  deleteAttachment(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/deleteAttachment/${id}`, '', { observe: 'response' });
  }

  protected convertDateFromClient(meetingDelivery: IMeetingDelivery): IMeetingDelivery {
    return Object.assign({}, meetingDelivery, {
      startDate: meetingDelivery.startDate?.isValid() ? meetingDelivery.startDate.format('YYYY-MM-DDTHH:mm:ssZ') : undefined,
      endDate: meetingDelivery.endDate?.isValid() ? meetingDelivery.endDate.format('YYYY-MM-DDTHH:mm:ssZ') : undefined,
    });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((meetingDelivery: IMeetingDelivery) => {
        (meetingDelivery.sentDate = meetingDelivery.sentDate ? dayjs(meetingDelivery.sentDate) : undefined),
          (meetingDelivery.startDate = meetingDelivery.startDate ? dayjs(meetingDelivery.startDate) : undefined),
          (meetingDelivery.endDate = meetingDelivery.endDate ? dayjs(meetingDelivery.endDate) : undefined),
          (meetingDelivery.createdDate = meetingDelivery.createdDate ? dayjs(meetingDelivery.createdDate) : undefined);
      });
    }
    return res;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      if (res.body.meetingDelivery) {
        res.body.meetingDelivery.sentDate = res.body.meetingDelivery.sentDate ? dayjs(res.body.meetingDelivery.sentDate) : undefined;
        res.body.meetingDelivery.startDate = res.body.meetingDelivery.startDate ? dayjs(res.body.meetingDelivery.startDate) : undefined;
        res.body.meetingDelivery.endDate = res.body.meetingDelivery.endDate ? dayjs(res.body.meetingDelivery.endDate) : undefined;
      }
    }
    return res;
  }
}
