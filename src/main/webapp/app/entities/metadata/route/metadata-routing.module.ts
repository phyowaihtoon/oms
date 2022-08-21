import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MetadataUpdateComponent } from '../update/metadata-update.component';
import { MetaDataComponent } from '../list/metadata.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MetaDataRoutingResolveService } from './metadata-routing-resolve.service';
import { MetaDataDetailComponent } from '../detail/metadata-detail.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: MetaDataComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MetaDataDetailComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MetadataUpdateComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MetadataUpdateComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class MetadataRoutingModule {}
