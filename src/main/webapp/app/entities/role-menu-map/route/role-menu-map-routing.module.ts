import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoleMenuMapComponent } from '../list/role-menu-map.component';
import { RoleMenuMapDetailComponent } from '../detail/role-menu-map-detail.component';
import { RoleMenuMapUpdateComponent } from '../update/role-menu-map-update.component';
import { RoleMenuMapRoutingResolveService } from './role-menu-map-routing-resolve.service';

const roleMenuMapRoute: Routes = [
  {
    path: '',
    component: RoleMenuMapComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleMenuMapDetailComponent,
    resolve: {
      roleMenuMap: RoleMenuMapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleMenuMapUpdateComponent,
    resolve: {
      roleMenuMap: RoleMenuMapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleMenuMapUpdateComponent,
    resolve: {
      roleMenuMap: RoleMenuMapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roleMenuMapRoute)],
  exports: [RouterModule],
})
export class RoleMenuMapRoutingModule {}
