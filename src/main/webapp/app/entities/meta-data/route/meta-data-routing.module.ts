import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MetaDataComponent } from '../list/meta-data.component';
import { MetaDataDetailComponent } from '../detail/meta-data-detail.component';
import { MetaDataUpdateComponent } from '../update/meta-data-update.component';
import { MetaDataRoutingResolveService } from './meta-data-routing-resolve.service';

const metaDataRoute: Routes = [
  {
    path: '',
    component: MetaDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MetaDataDetailComponent,
    resolve: {
      metaData: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MetaDataUpdateComponent,
    resolve: {
      metaData: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MetaDataUpdateComponent,
    resolve: {
      metaData: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(metaDataRoute)],
  exports: [RouterModule],
})
export class MetaDataRoutingModule {}
