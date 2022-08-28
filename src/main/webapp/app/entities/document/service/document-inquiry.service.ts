import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDocumentHeader } from '../document.model';
import { createRequestOption } from 'app/core/request/request-util';

export type EntityArrayResponseType = HttpResponse<IDocumentHeader[]>;
export type EntityResponseType = HttpResponse<IDocumentHeader>;

@Injectable({
  providedIn: 'root',
})
export class DocumentInquiryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/docinquiry');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(criteriaData: IDocumentHeader, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<IDocumentHeader[]>(`${this.resourceUrl}`, criteriaData, { params: options, observe: 'response' });
  }

  getDocumentsById(id: number): Observable<EntityResponseType> {
    return this.http.get(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
