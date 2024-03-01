import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnouncementComponent } from './list/announcement.component';
import { AnnouncementDetailComponent } from './detail/announcement-detail.component';
import { AnnouncementUpdateComponent } from './update/announcement-update.component';
import { AnnouncementDeleteDialogComponent } from './delete/announcement-delete-dialog.component';
import { AnnouncementRoutingModule } from './route/announcement-routing.module';

@NgModule({
  imports: [SharedModule, AnnouncementRoutingModule],
  declarations: [AnnouncementComponent, AnnouncementDetailComponent, AnnouncementUpdateComponent, AnnouncementDeleteDialogComponent],
  entryComponents: [AnnouncementDeleteDialogComponent],
})
export class AnnouncementModule {}
