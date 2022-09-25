import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMetaData, IMetaDataHeader } from '../metadata/metadata.model';
import { IRepositoryInquiry, IRepositoryHeader } from '../repository/repository.model';
import { createRequestOption } from 'app/core/request/request-util';

export type MeataDataHeaderSetupArray = HttpResponse<IMetaDataHeader[]>;
export type MetaDataSetupArray = HttpResponse<IMetaData[]>;
export type RepositoryHeaderArrayType = HttpResponse<IRepositoryHeader[]>;
export type WorkflowAuthorityArrayType = HttpResponse<IWorkflowAuthority[]>;

import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { IWorkflowAuthority } from './setup.model';

@Injectable({
  providedIn: 'root',
})
export class LoadSetupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/setup');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadAllMetaDataHeader(): Observable<MeataDataHeaderSetupArray> {
    const childURL = '/metadataheader';
    return this.http.get<IMetaDataHeader[]>(this.resourceUrl + childURL, { observe: 'response' });
  }

  loadAllMetaDatabyMetadatHeaderId(id: number): Observable<MetaDataSetupArray> {
    const childURL = this.resourceUrl + '/metadata/' + id.toString();
    return this.http.get<IMetaData[]>(childURL, { observe: 'response' });
  }

  loadRepository(criteriaData: IRepositoryInquiry, req?: any): Observable<RepositoryHeaderArrayType> {
    const options = createRequestOption(req);
    return this.http
      .post<IRepositoryHeader[]>(this.resourceUrl + '/repository', criteriaData, { params: options, observe: 'response' })
      .pipe(map((res: RepositoryHeaderArrayType) => this.convertDateArrayFromServer(res)));
  }

  convertDateArrayFromServer(res: RepositoryHeaderArrayType): RepositoryHeaderArrayType {
    if (res.body) {
      res.body.forEach((documentHeader: IRepositoryHeader) => {
        documentHeader.createdDate = documentHeader.createdDate ? dayjs(documentHeader.createdDate) : undefined;
      });
    }
    return res;
  }

  loadWorkflowAuthority(): Observable<WorkflowAuthorityArrayType> {
    const childURL = '/workflow';
    return this.http.get<IWorkflowAuthority[]>(this.resourceUrl + childURL, { observe: 'response' });
  }
}
