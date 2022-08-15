import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DocumentSearchComponent } from '../search/document-search.component';
import { DocumentUpdateComponent } from '../update/document-update.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: DocumentUpdateComponent,
  },
  {
    path: 'search',
    component: DocumentSearchComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class DocumentRoutingModule {}
