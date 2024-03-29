import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';

export type WorkflowAuthorityArrayType = HttpResponse<IWorkflowAuthority[]>;
export type DocumentStatusArrayType = HttpResponse<IDocumentStatus[]>;
export type PriorityArrayType = HttpResponse<IPriority[]>;
export type DashboardArrayType = HttpResponse<IDashboardTemplate[]>;
export type CodeTypeArrayType = HttpResponse<ICodeType[]>;
export type HeadDepartmentType = HttpResponse<IHeadDepartment[]>;
export type SubDepartmentType = HttpResponse<IDepartment[]>;
export type DraftSummaryType = HttpResponse<IDraftSummary>;
export type AnnouncementType = HttpResponse<IAnnouncement[]>;

import { ICodeType, IDocumentStatus, IDraftSummary, IPriority, IWorkflowAuthority } from './setup.model';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';
import { IDepartment, IHeadDepartment } from '../department/department.model';
import { IUser } from '../user/user.model';
import { IAnnouncement } from '../announcement/announcement.model';

@Injectable({
  providedIn: 'root',
})
export class LoadSetupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/setup');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadPriority(): Observable<PriorityArrayType> {
    const childURL = '/priority';
    return this.http.get<IPriority[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllDashboardTemplate(): Observable<DashboardArrayType> {
    const childURL = '/dashboard';
    return this.http.get<IDashboardTemplate[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllHeadDepartments(): Observable<HeadDepartmentType> {
    const childURL = '/headdept';
    return this.http.get<IHeadDepartment[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllSubDepartments(): Observable<SubDepartmentType> {
    const childURL = '/subdept';
    return this.http.get<IDepartment[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadDraftSummary(): Observable<DraftSummaryType> {
    const childURL = '/draftsummary';
    return this.http.get<IDraftSummary>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllAnnouncement(): Observable<AnnouncementType> {
    const childURL = '/announcement';
    return this.http.get<IAnnouncement[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllUsers(): Observable<HttpResponse<IUser[]>> {
    const childURL = '/users';
    return this.http.get<IUser[]>(this.resourceUrl + childURL, { observe: 'response' });
  }
}
