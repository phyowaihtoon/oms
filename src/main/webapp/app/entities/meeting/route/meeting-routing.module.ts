import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MeetingUpdateComponent } from '../update/meeting-update.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeetingRoutingResolveService } from './meeting-routing-resolve.service';
import { MeetingDetailComponent } from '../detail/meeting-detail.component';
import { MeetingReceivedComponent } from '../received/meeting-received.component';
import { MeetingSentComponent } from '../sent/meeting-sent.component';
import { MeetingDraftComponent } from '../draft/meeting-draft.component';

const meetingRoute: Routes = [
  {
    path: 'sent',
    component: MeetingSentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'received',
    component: MeetingReceivedComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'draft',
    component: MeetingDraftComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeetingDetailComponent,
    resolve: {
      meeting: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeetingUpdateComponent,
    resolve: {
      department: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeetingUpdateComponent,
    resolve: {
      department: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(meetingRoute)],
  exports: [RouterModule],
})
export class MeetingRoutingModule {}
