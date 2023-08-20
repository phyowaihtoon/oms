import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { MeetingMessage, IMeetingMessage } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';

@Injectable({
  providedIn: 'root',
})
export class MeetingRoutingResolveService implements Resolve<IMeetingMessage> {
  constructor(protected service: MeetingService, protected router: Router) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): IMeetingMessage | Observable<IMeetingMessage> | Promise<IMeetingMessage> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((meeting: HttpResponse<IMeetingMessage>) => {
          if (meeting.body) {
            return of(meeting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeetingMessage());
  }
}
