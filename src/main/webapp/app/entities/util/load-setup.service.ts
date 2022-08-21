import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IMetaDataHeader } from '../metadata/metadata.model';

export type MeataDataHeaderSetupArray = HttpResponse<IMetaDataHeader[]>;

@Injectable({
  providedIn: 'root',
})
export class LoadSetupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/setup');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadAllMetaDataHeader(): Observable<MeataDataHeaderSetupArray> {
    const childURL = '/metadataheader';
    console.log('Calling Load Setup Service');
    return this.http.get<IMetaDataHeader[]>(this.resourceUrl + childURL, { observe: 'response' });
  }
}
