import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDocumentInquiry } from '../document.model';
import { createRequestOption } from 'app/core/request/request-util';
export type EntityArrayResponseType = HttpResponse<IDocumentInquiry[]>;

@Injectable({
  providedIn: 'root',
})
export class DocumentInquiryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/docinquiry');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentInquiry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
