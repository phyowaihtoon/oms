import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DocumentHeaderComponent } from './list/document-header.component';
import { DocumentHeaderDetailComponent } from './detail/document-header-detail.component';
import { DocumentHeaderUpdateComponent } from './update/document-header-update.component';
import { DocumentHeaderDeleteDialogComponent } from './delete/document-header-delete-dialog.component';
import { DocumentHeaderRoutingModule } from './route/document-header-routing.module';

@NgModule({
  imports: [SharedModule, DocumentHeaderRoutingModule],
  declarations: [
    DocumentHeaderComponent,
    DocumentHeaderDetailComponent,
    DocumentHeaderUpdateComponent,
    DocumentHeaderDeleteDialogComponent,
  ],
  entryComponents: [DocumentHeaderDeleteDialogComponent],
})
export class DocumentHeaderModule {}
