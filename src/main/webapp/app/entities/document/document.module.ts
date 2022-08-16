import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentRoutingModule } from './route/document-routing.module';
import { DocumentUpdateComponent } from './update/document-update.component';
import { DocumentSearchComponent } from './search/document-search.component';
import { DocumentComponent } from './list/document.component';

@NgModule({
  imports: [SharedModule, DocumentRoutingModule],
  declarations: [DocumentUpdateComponent, DocumentSearchComponent, DocumentComponent],
})
export class DocumentModule {}
