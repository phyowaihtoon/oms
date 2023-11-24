import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { Observable } from 'rxjs';
import { IRptParamsDTO } from '../report.model';

export type EntityResponseType = HttpResponse<IReplyMessage>;

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  public docMappingListRptURL = this.applicationConfigService.getEndpointFor('api/docmap-rpt');
  public docMappingListRptURL2 = this.applicationConfigService.getEndpointFor('api/doclist-rpt');
  public showPDFUrl = this.applicationConfigService.getEndpointFor('api/viewPdf');
  public downloadUrl = this.applicationConfigService.getEndpointFor('api/download');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  generateDocMappingListRpt(reportDTO: IRptParamsDTO): Observable<EntityResponseType> {
    return this.http.post<IReplyMessage>(this.docMappingListRptURL, reportDTO, { observe: 'response' });
  }

  generateDocMappingListRpt2(reportDTO: IRptParamsDTO): Observable<EntityResponseType> {
    return this.http.post<IReplyMessage>(this.docMappingListRptURL, reportDTO, { observe: 'response' });
  }

  showPDF(fileName: string): Observable<Blob> {
    return this.http.get(`${this.showPDFUrl}/${fileName}`, { responseType: 'blob' });
  }

  downloadFile(fileName: string): Observable<Blob> {
    return this.http.get(`${this.downloadUrl}/${fileName}`, { responseType: 'blob' });
  }
}
