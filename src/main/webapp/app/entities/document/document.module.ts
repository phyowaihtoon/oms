import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentRoutingModule } from './route/document-routing.module';
import { DocumentUpdateComponent } from './update/document-update.component';

@NgModule({
  imports: [SharedModule, DocumentRoutingModule],
  declarations: [DocumentUpdateComponent],
})
export class DocumentModule {}
