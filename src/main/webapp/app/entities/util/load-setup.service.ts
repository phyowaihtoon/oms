import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMetaData, IMetaDataHeader } from '../metadata/metadata.model';

export type MeataDataHeaderSetupArray = HttpResponse<IMetaDataHeader[]>;
export type MetaDataSetupArray = HttpResponse<IMetaData[]>;

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
}
