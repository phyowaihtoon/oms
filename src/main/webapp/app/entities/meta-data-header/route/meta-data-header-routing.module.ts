import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MetaDataHeaderComponent } from '../list/meta-data-header.component';
import { MetaDataHeaderDetailComponent } from '../detail/meta-data-header-detail.component';
import { MetaDataHeaderUpdateComponent } from '../update/meta-data-header-update.component';
import { MetaDataHeaderRoutingResolveService } from './meta-data-header-routing-resolve.service';

const metaDataHeaderRoute: Routes = [
  {
    path: '',
    component: MetaDataHeaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MetaDataHeaderDetailComponent,
    resolve: {
      metaDataHeader: MetaDataHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MetaDataHeaderUpdateComponent,
    resolve: {
      metaDataHeader: MetaDataHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MetaDataHeaderUpdateComponent,
    resolve: {
      metaDataHeader: MetaDataHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(metaDataHeaderRoute)],
  exports: [RouterModule],
})
export class MetaDataHeaderRoutingModule {}
