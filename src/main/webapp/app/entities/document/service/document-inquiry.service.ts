import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDocumentHeader, IDocumentInquiry } from '../document.model';
import { createRequestOption } from 'app/core/request/request-util';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

export type EntityArrayResponseType = HttpResponse<IDocumentHeader[]>;
export type EntityResponseType = HttpResponse<IDocumentHeader>;
export type ReplyMessageType = HttpResponse<IReplyMessage>;
export type BlobType = HttpResponse<Blob>;

@Injectable({
  providedIn: 'root',
})
export class DocumentInquiryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/docinquiry');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(criteriaData: IDocumentInquiry, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IDocumentHeader[]>(`${this.resourceUrl}`, criteriaData, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getDocumentsById(id: number): Observable<EntityResponseType> {
    return this.http
      .get(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  downloadFile(docId: number): Observable<BlobType> {
    return this.http.get(`${this.resourceUrl}/download/${docId}`, { observe: 'response', responseType: 'blob' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentHeader: IDocumentHeader) => {
        documentHeader.createdDate = documentHeader.createdDate ? dayjs(documentHeader.createdDate) : undefined;
      });
    }
    return res;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
    }
    return res;
  }
}
