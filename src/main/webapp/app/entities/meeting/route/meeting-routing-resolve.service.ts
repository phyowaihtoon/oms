import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { IMeetingDelivery, MeetingDelivery } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';

@Injectable({
  providedIn: 'root',
})
export class MeetingRoutingResolveService implements Resolve<IMeetingDelivery> {
  constructor(protected service: MeetingService, protected router: Router) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): IMeetingDelivery | Observable<IMeetingDelivery> | Promise<IMeetingDelivery> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((delivery: HttpResponse<MeetingDelivery>) => {
          if (delivery.body) {
            return of(delivery.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeetingDelivery());
  }
}
