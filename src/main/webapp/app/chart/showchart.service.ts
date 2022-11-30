import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

type EntityArrayResponseType = HttpResponse<[]>;

@Injectable({ providedIn: 'root' })
export class SchowChartService {
  public resourceUrl = SERVER_API_URL + 'api/dashboard';

  constructor(protected http: HttpClient) {}

  getData(url: String, inputParam: any): Observable<EntityArrayResponseType> {
    return this.http.post<[]>(this.resourceUrl + url, inputParam, { observe: 'response' });
  }
}
