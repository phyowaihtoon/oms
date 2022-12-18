import { HttpClient, HttpEvent, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { Observable } from 'rxjs';
import { getDocIdentifier, IDocumentHeader, IDocumentInquiry } from '../document.model';

export type EntityArrayResponseType = HttpResponse<IMetaDataHeader[]>;
export type EntityResponseType = HttpResponse<IDocumentHeader>;

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/documents');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  createAndUploadDocuments(formData: FormData): Observable<HttpResponse<IReplyMessage>> {
    return this.http.post<IReplyMessage>(this.resourceUrl, formData, { observe: 'response' });
  }

  updateAndUploadDocuments(formData: FormData, id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.put<IReplyMessage>(`${this.resourceUrl}/${id}`, formData, {
      observe: 'response',
    });
  }

  updateDocumentStatus(documentInquiry: IDocumentInquiry, id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.patch<IReplyMessage>(`${this.resourceUrl}/${id}`, documentInquiry, {
      observe: 'response',
    });
  }

  restoreDocument(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.patch<IReplyMessage>(
      `${this.resourceUrl}/restore/${id}`,
      {},
      {
        observe: 'response',
      }
    );
  }

  deleteFile(id: number): Observable<HttpResponse<IReplyMessage>> {
    return this.http.get<IReplyMessage>(`${this.resourceUrl}/deleteFileById/${id}`, { observe: 'response' });
  }
}
