import { NgModule } from '@angular/core';
import { ServiceQueueRoutingModule } from './service-queue-routing.module';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentQueueUpdateComponent } from './document/update/document-queue-update.component';
import { DocumentQueueComponent } from './document/list/document-queue.component';
import { ApproveRejectRemarkComponent } from './document/approve-reject-remark/approve-reject-remark.component';

@NgModule({
  imports: [SharedModule, ServiceQueueRoutingModule],
  declarations: [DocumentQueueComponent, DocumentQueueUpdateComponent, ApproveRejectRemarkComponent],
})
export class ServiceQueueModule {}
