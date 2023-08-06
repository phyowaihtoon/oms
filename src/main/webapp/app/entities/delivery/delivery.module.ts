import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryUpdateComponent } from './update/delivery-update.component';
import { DeliveryDetailComponent } from './detail/delivery-detail.component';
import { DeliverySentComponent } from './sent/delivery-sent.component';
import { DeliveryReceivedComponent } from './received/delivery-received.component';
import { DeliveryRoutingModule } from './route/delivery-routing.module';
import { DeliveryDraftComponent } from './draft/delivery-draft.component';

@NgModule({
  imports: [SharedModule, DeliveryRoutingModule],
  declarations: [
    DeliveryUpdateComponent,
    DeliveryDetailComponent,
    DeliverySentComponent,
    DeliveryReceivedComponent,
    DeliveryDraftComponent,
  ],
})
export class DeliveryModule {}
