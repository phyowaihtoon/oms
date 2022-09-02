import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { EntityResponseType } from 'app/entities/category/service/category.service';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { Observable } from 'rxjs';
import { IDocumentHeader } from '../document.model';

export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/categories');
  public resourceUrl2 = this.applicationConfigService.getEndpointFor('api/documents');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getAllMetaDataHeader(): Observable<EntityArrayResponseType> {
    return this.http.get<IMetaDataHeader[]>(`${this.resourceUrl}`, { observe: 'response' });
  }

  create(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.post<IDocumentHeader>(this.resourceUrl, documentHeader, { observe: 'response' });
  }
}
