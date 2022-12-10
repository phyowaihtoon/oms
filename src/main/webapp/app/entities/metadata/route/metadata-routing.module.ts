import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MetadataUpdateComponent } from '../update/metadata-update.component';
import { MetaDataComponent } from '../list/metadata.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MetaDataRoutingResolveService } from './metadata-routing-resolve.service';
import { MetaDataDetailComponent } from '../detail/metadata-detail.component';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { MetadataTrashbinComponent } from '../trashbin/metadata-trashbin.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: MetaDataComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      defaultSort: 'id,asc',
      menuCode: 'METADI',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MetadataUpdateComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'METADC',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MetadataUpdateComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'METADC',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MetaDataDetailComponent,
    resolve: {
      metadata: MetaDataRoutingResolveService,
    },
  },
  {
    path: 'trashbin',
    component: MetadataTrashbinComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      defaultSort: 'id,asc',
      menuCode: 'METADTB',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class MetadataRoutingModule {}
