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

import { ICodeType, IDocumentStatus, IPriority, IWorkflowAuthority } from './setup.model';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';
import { IDepartment, IHeadDepartment } from '../department/department.model';

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
}
