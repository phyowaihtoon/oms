import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentRoutingModule } from './route/document-routing.module';
import { DocumentUpdateComponent } from './update/document-update.component';
import { DocumentComponent } from './list/document.component';
import { DocumentDetailComponent } from './detail/document-detail.component';
import { InfoPopupComponent } from '../util/infopopup/info-popup.component';
import { RepositoryDialogComponent } from '../util/repositorypopup/repository-dialog.component';
import { DocumentTrashbinComponent } from './trashbin/list/document-trashbin.component';
import { DocumentRestoreDialogComponent } from './restore/document-restore-dialog.component';
import { DocumentTrashbinDetailComponent } from './trashbin/detail/document-trashbin-detail.component';
import { DocumentDeleteDialogComponent } from './delete/document-delete-dialog.component';

@NgModule({
  imports: [SharedModule, DocumentRoutingModule],
  declarations: [
    DocumentUpdateComponent,
    DocumentComponent,
    DocumentDetailComponent,
    InfoPopupComponent,
    RepositoryDialogComponent,
    DocumentTrashbinComponent,
    DocumentRestoreDialogComponent,
    DocumentTrashbinDetailComponent,
    DocumentDeleteDialogComponent,
  ],
  entryComponents: [DocumentDeleteDialogComponent],
})
export class DocumentModule {}
