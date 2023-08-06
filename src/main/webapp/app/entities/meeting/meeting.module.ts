import { NgModule } from '@angular/core';
import { MeetingUpdateComponent } from './update/meeting-update.component';
import { MeetingRoutingModule } from './route/meeting-routing.module';
import { SharedModule } from 'app/shared/shared.module';
import { MeetingDetailComponent } from './detail/meeting-detail.component';
import { MeetingReceivedComponent } from './received/meeting-received.component';
import { MeetingSentComponent } from './sent/meeting-sent.component';
import { MeetingDraftComponent } from './draft/meeting-draft.component';

@NgModule({
  declarations: [MeetingUpdateComponent, MeetingDetailComponent, MeetingReceivedComponent, MeetingSentComponent, MeetingDraftComponent],
  imports: [SharedModule, MeetingRoutingModule],
})
export class MeetingModule {}
