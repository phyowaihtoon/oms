import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnouncement, getAnnouncementIdentifier } from '../announcement.model';

export type EntityResponseType = HttpResponse<IAnnouncement>;
export type EntityArrayResponseType = HttpResponse<IAnnouncement[]>;

@Injectable({ providedIn: 'root' })
export class AnnouncementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/announcements');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(announcement: IAnnouncement): Observable<EntityResponseType> {
    return this.http.post<IAnnouncement>(this.resourceUrl, announcement, { observe: 'response' });
  }

  update(announcement: IAnnouncement): Observable<EntityResponseType> {
    return this.http.put<IAnnouncement>(`${this.resourceUrl}/${getAnnouncementIdentifier(announcement) as number}`, announcement, {
      observe: 'response',
    });
  }

  partialUpdate(announcement: IAnnouncement): Observable<EntityResponseType> {
    return this.http.patch<IAnnouncement>(`${this.resourceUrl}/${getAnnouncementIdentifier(announcement) as number}`, announcement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnnouncement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnouncement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnnouncementToCollectionIfMissing(
    announcementCollection: IAnnouncement[],
    ...announcementsToCheck: (IAnnouncement | null | undefined)[]
  ): IAnnouncement[] {
    const announcements: IAnnouncement[] = announcementsToCheck.filter(isPresent);
    if (announcements.length > 0) {
      const announcementCollectionIdentifiers = announcementCollection.map(
        announcementItem => getAnnouncementIdentifier(announcementItem)!
      );
      const announcementsToAdd = announcements.filter(announcementItem => {
        const announcementIdentifier = getAnnouncementIdentifier(announcementItem);
        if (announcementIdentifier == null || announcementCollectionIdentifiers.includes(announcementIdentifier)) {
          return false;
        }
        announcementCollectionIdentifiers.push(announcementIdentifier);
        return true;
      });
      return [...announcementsToAdd, ...announcementCollection];
    }
    return announcementCollection;
  }
}
