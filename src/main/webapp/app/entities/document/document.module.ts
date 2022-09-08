import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentRoutingModule } from './route/document-routing.module';
import { DocumentUpdateComponent } from './update/document-update.component';
import { DocumentComponent } from './list/document.component';
import { DocumentDetailComponent } from './detail/document-detail.component';
import { InfoPopupComponent } from '../util/infopopup/info-popup.component';

@NgModule({
  imports: [SharedModule, DocumentRoutingModule],
  declarations: [DocumentUpdateComponent, DocumentComponent, DocumentDetailComponent, InfoPopupComponent],
})
export class DocumentModule {}
