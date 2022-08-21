import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DocumentDetailComponent } from '../detail/document-detail.component';
import { DocumentComponent } from '../list/document.component';
import { DocumentUpdateComponent } from '../update/document-update.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: DocumentUpdateComponent,
  },
  {
    path: 'list',
    component: DocumentComponent,
  },
  {
    path: ':id/view',
    component: DocumentDetailComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class DocumentRoutingModule {}
