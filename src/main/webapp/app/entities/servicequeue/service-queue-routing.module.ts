import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentQueueComponent } from './document/list/document-queue.component';
import { DocumentQueueUpdateComponent } from './document/update/document-queue-update.component';

const serviceQueueRoute: Routes = [
  {
    path: 'document',
    component: DocumentQueueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'document/id',
    component: DocumentQueueUpdateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceQueueRoute)],
  exports: [RouterModule],
})
export class ServiceQueueRoutingModule {}