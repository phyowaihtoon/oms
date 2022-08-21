import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MetadataRoutingModule } from './route/metadata-routing.module';
import { MetadataUpdateComponent } from './update/metadata-update.component';
import { MetaDataComponent } from './list/metadata.component';
import { MetaDataDetailComponent } from './detail/metadata-detail.component';
import { MetaDataDeleteDialogComponent } from './delete/metadata-delete-dialog.component';

@NgModule({
  imports: [SharedModule, MetadataRoutingModule],
  declarations: [MetaDataComponent, MetaDataDetailComponent, MetadataUpdateComponent, MetaDataDeleteDialogComponent],
  entryComponents: [MetaDataDeleteDialogComponent],
})
export class MetadataModule {}
