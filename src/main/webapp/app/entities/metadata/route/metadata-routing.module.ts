import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MetadataUpdateComponent } from '../update/metadata-update.component';

const metadataRoute: Routes = [
  {
    path: '',
    component: MetadataUpdateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class MetadataRoutingModule {}
