import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { DocumentQueueComponent } from './document/list/document-queue.component';
import { DocumentQueueUpdateComponent } from './document/update/document-queue-update.component';
import { ServiceQueueRoutingResolveService } from './service-queue-routing-resolve.service';

const serviceQueueRoute: Routes = [
  {
    path: 'document',
    component: DocumentQueueComponent,
    data: {
      defaultSort: 'id,asc',
      menuCode: 'DOCQ',
    },
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'document/:id/view',
    component: DocumentQueueUpdateComponent,
    data: {
      defaultSort: 'id,asc',
      menuCode: 'DOCQ',
    },
    resolve: {
      docHeader: ServiceQueueRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceQueueRoute)],
  exports: [RouterModule],
})
export class ServiceQueueRoutingModule {}
