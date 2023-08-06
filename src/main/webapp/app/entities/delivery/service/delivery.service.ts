import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDelivery } from '../delivery.model';
import { Observable } from 'rxjs';

export type EntityResponseType = HttpResponse<IDelivery>;
export type EntityArrayResponseType = HttpResponse<IDelivery[]>;

@Injectable({
  providedIn: 'root',
})
export class DeliveryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDelivery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
