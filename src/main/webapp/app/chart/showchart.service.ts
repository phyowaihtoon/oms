import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';
import { IPieHeaderDataDto, PieHeaderDataDto } from 'app/services/pieheaderdata.model';

type EntityArrayResponseType = HttpResponse<[]>;
type EntityArrayResponseType2 = HttpResponse<IDashboardTemplate[]>;
export type PieResponseType = HttpResponse<IPieHeaderDataDto>;

@Injectable({ providedIn: 'root' })
export class SchowChartService {
  public resourceUrl = SERVER_API_URL + 'api/dashboard';

  constructor(protected http: HttpClient) {}

  getData(url: string, inputParam: any): Observable<EntityArrayResponseType> {
    return this.http.post<[]>(this.resourceUrl + url, inputParam, { observe: 'response' });
  }

  getAllTemplate(): Observable<EntityArrayResponseType2> {
    return this.http.get<IDashboardTemplate[]>(this.resourceUrl + '/allTemplate', { observe: 'response' });
  }

  getAllSummaryData(): Observable<PieResponseType> {
    return this.http.get<IPieHeaderDataDto>(this.resourceUrl + '/getAllSummary', { observe: 'response' });
  }

  getTodaySummaryData(): Observable<PieResponseType> {
    return this.http.get<IPieHeaderDataDto>(this.resourceUrl + '/getTodaySummary', { observe: 'response' });
  }

  getDataByTemplate(inputParam: any): Observable<EntityArrayResponseType> {
    return this.http.post<[]>(this.resourceUrl + '/getDataByTemplate', inputParam, { observe: 'response' });
  }

  getTodaySummaryByTemplate(inputParam: any): Observable<PieResponseType> {
    return this.http.post<IPieHeaderDataDto>(this.resourceUrl + '/getTodaySummaryByTemplate', inputParam, { observe: 'response' });
  }
}
