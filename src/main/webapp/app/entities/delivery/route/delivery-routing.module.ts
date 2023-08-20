import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeliveryUpdateComponent } from '../update/delivery-update.component';
import { DeliveryRoutingResolveService } from './delivery-routing-resolve.service';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryDetailComponent } from '../detail/delivery-detail.component';
import { DeliverySentComponent } from '../sent/delivery-sent.component';
import { DeliveryReceivedComponent } from '../received/delivery-received.component';
import { DeliveryDraftComponent } from '../draft/delivery-draft.component';

const deliveryRoute: Routes = [
  {
    path: 'sent',
    component: DeliverySentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'received',
    component: DeliveryReceivedComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'draft',
    component: DeliveryDraftComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryDetailComponent,
    resolve: {
      delivery: DeliveryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryUpdateComponent,
    resolve: {
      delivery: DeliveryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryUpdateComponent,
    resolve: {
      delivery: DeliveryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryRoute)],
  exports: [RouterModule],
})
export class DeliveryRoutingModule {}
