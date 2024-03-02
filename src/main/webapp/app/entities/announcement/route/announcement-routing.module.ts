import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnouncementComponent } from '../list/announcement.component';
import { AnnouncementDetailComponent } from '../detail/announcement-detail.component';
import { AnnouncementUpdateComponent } from '../update/announcement-update.component';
import { AnnouncementRoutingResolveService } from './announcement-routing-resolve.service';

const announcementRoute: Routes = [
  {
    path: '',
    component: AnnouncementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnouncementDetailComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnouncementUpdateComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnouncementUpdateComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(announcementRoute)],
  exports: [RouterModule],
})
export class AnnouncementRoutingModule {}
