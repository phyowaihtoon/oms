import { HttpClient, HttpEvent, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { EntityResponseType } from 'app/entities/category/service/category.service';
import { IMetaData, IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { Observable } from 'rxjs';
import { getDocIdentifier, IDocumentHeader } from '../document.model';

export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/categories');
  public resourceUrl2 = this.applicationConfigService.getEndpointFor('api/documentHeader');
  public resourceUrl3 = this.applicationConfigService.getEndpointFor('api/documents');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getAllMetaDataHeader(): Observable<EntityArrayResponseType> {
    return this.http.get<IMetaDataHeader[]>(`${this.resourceUrl}`, { observe: 'response' });
  }

  create(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    console.log('inside create()');
    return this.http.post<IDocumentHeader>(this.resourceUrl2, documentHeader, { observe: 'response' });
  }

  update(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.put<IDocumentHeader>(`${this.resourceUrl2}/${getDocIdentifier(documentHeader) as number}`, documentHeader, {
      observe: 'response',
    });
  }

  partialUpdate(documentHeader: IDocumentHeader): Observable<EntityResponseType> {
    return this.http.patch<IDocumentHeader>(`${this.resourceUrl}/${getDocIdentifier(documentHeader) as number}`, documentHeader, {
      observe: 'response',
    });
  }

  // define function to Upload files
  upload(formData: FormData): Observable<HttpEvent<string[]>> {
    const childURL = '/upload';
    return this.http.post<string[]>(this.resourceUrl3 + childURL, formData, {
      reportProgress: true,
      observe: 'events',
    });
  }

  // define function to download files
  download(filename: string): Observable<HttpEvent<Blob>> {
    const childURL = '/metadataheader';
    return this.http.get(this.resourceUrl2 + childURL, {
      reportProgress: true,
      observe: 'events',
      responseType: 'blob',
    });
  }
}
