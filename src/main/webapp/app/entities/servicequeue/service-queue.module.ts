import { NgModule } from '@angular/core';
import { ServiceQueueRoutingModule } from './service-queue-routing.module';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentQueueUpdateComponent } from './document/update/document-queue-update.component';
import { DocumentQueueComponent } from './document/list/document-queue.component';

@NgModule({
  imports: [SharedModule, ServiceQueueRoutingModule],
  declarations: [DocumentQueueComponent, DocumentQueueUpdateComponent],
})
export class ServiceQueueModule {}
