import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMeeting } from '../meeting.model';

export type EntityResponseType = HttpResponse<IMeeting>;
export type EntityArrayResponseType = HttpResponse<IMeeting[]>;

@Injectable({
  providedIn: 'root',
})
export class MeetingService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/meeting');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeeting>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
