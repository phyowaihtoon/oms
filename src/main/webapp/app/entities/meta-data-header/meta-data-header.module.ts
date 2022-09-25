import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MetaDataHeaderComponent } from './list/meta-data-header.component';
import { MetaDataHeaderDetailComponent } from './detail/meta-data-header-detail.component';
import { MetaDataHeaderUpdateComponent } from './update/meta-data-header-update.component';
import { MetaDataHeaderDeleteDialogComponent } from './delete/meta-data-header-delete-dialog.component';
import { MetaDataHeaderRoutingModule } from './route/meta-data-header-routing.module';

@NgModule({
  imports: [SharedModule, MetaDataHeaderRoutingModule],
  declarations: [
    MetaDataHeaderComponent,
    MetaDataHeaderDetailComponent,
    MetaDataHeaderUpdateComponent,
    MetaDataHeaderDeleteDialogComponent,
  ],
  entryComponents: [MetaDataHeaderDeleteDialogComponent],
})
export class MetaDataHeaderModule {}
