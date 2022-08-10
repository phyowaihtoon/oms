import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MetadataRoutingModule } from './route/metadata-routing.module';
import { MetadataUpdateComponent } from './update/metadata-update.component';

@NgModule({
  imports: [SharedModule, MetadataRoutingModule],
  declarations: [MetadataUpdateComponent],
})
export class MetadataModule {}
