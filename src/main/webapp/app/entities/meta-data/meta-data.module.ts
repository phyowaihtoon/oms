import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MetaDataComponent } from './list/meta-data.component';
import { MetaDataDetailComponent } from './detail/meta-data-detail.component';
import { MetaDataUpdateComponent } from './update/meta-data-update.component';
import { MetaDataDeleteDialogComponent } from './delete/meta-data-delete-dialog.component';
import { MetaDataRoutingModule } from './route/meta-data-routing.module';

@NgModule({
  imports: [SharedModule, MetaDataRoutingModule],
  declarations: [MetaDataComponent, MetaDataDetailComponent, MetaDataUpdateComponent, MetaDataDeleteDialogComponent],
  entryComponents: [MetaDataDeleteDialogComponent],
})
export class MetaDataModule {}
