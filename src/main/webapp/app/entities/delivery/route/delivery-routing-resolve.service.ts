import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { DeliveryMessage, IDeliveryMessage } from '../delivery.model';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { DeliveryService } from '../service/delivery.service';

@Injectable({
  providedIn: 'root',
})
export class DeliveryRoutingResolveService implements Resolve<IDeliveryMessage> {
  constructor(protected service: DeliveryService, protected router: Router) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): IDeliveryMessage | Observable<IDeliveryMessage> | Promise<IDeliveryMessage> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((delivery: HttpResponse<IDeliveryMessage>) => {
          if (delivery.body) {
            return of(delivery.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryMessage());
  }
}
