import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';
import { IReplyMessage } from '../util/reply-message.model';
import { ISysConfig } from './sys-config.model';

export type ReplyMessageType = HttpResponse<IReplyMessage>;

@Injectable({
  providedIn: 'root',
})
export class SysConfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sysconfig');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadAllSysConfig(req?: any): Observable<ReplyMessageType> {
    return this.http.get<IReplyMessage>(this.resourceUrl, { observe: 'response' });
  }

  update(configList: ISysConfig[]): Observable<ReplyMessageType> {
    return this.http.post<IReplyMessage>(this.resourceUrl, configList, { observe: 'response' });
  }
}
