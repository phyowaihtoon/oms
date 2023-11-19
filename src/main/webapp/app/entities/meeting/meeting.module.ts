import { NgModule } from '@angular/core';
import { MeetingUpdateComponent } from './update/meeting-update.component';
import { MeetingRoutingModule } from './route/meeting-routing.module';
import { SharedModule } from 'app/shared/shared.module';
import { MeetingDetailComponent } from './detail/meeting-detail.component';
import { MeetingReceivedComponent } from './received/meeting-received.component';
import { MeetingSentComponent } from './sent/meeting-sent.component';
import { MeetingDraftComponent } from './draft/meeting-draft.component';
import { QuillModule } from 'ngx-quill';
import { DocumentDeleteDialogComponent } from './delete/document-delete-dialog/document-delete-dialog.component';

@NgModule({
  declarations: [MeetingUpdateComponent, MeetingDetailComponent, MeetingReceivedComponent, MeetingSentComponent, MeetingDraftComponent, DocumentDeleteDialogComponent],
  imports: [SharedModule, MeetingRoutingModule,
    QuillModule.forRoot()],
})
export class MeetingModule {}
