import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplicationUserComponent } from '../list/application-user.component';
import { ApplicationUserDetailComponent } from '../detail/application-user-detail.component';
import { ApplicationUserUpdateComponent } from '../update/application-user-update.component';
import { ApplicationUserRoutingResolveService } from './application-user-routing-resolve.service';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';

const applicationUserRoute: Routes = [
  {
    path: '',
    component: ApplicationUserComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      defaultSort: 'id,asc',
      menuCode: 'UAUTH',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationUserDetailComponent,
    resolve: {
      applicationUser: ApplicationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUserUpdateComponent,
    resolve: {
      applicationUser: ApplicationUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applicationUserRoute)],
  exports: [RouterModule],
})
export class ApplicationUserRoutingModule {}
