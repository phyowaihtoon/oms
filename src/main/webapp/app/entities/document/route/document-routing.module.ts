import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DocumentUpdateComponent } from '../update/document-update.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: DocumentUpdateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class DocumentRoutingModule {}
