import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IMeetingAttachment, IMeetingDelivery, IMeetingReceiver } from '../meeting.model';

@Component({
  selector: 'jhi-meeting-detail',
  templateUrl: './meeting-detail.component.html',
  styleUrls: ['./meeting-detail.component.scss'],
})
export class MeetingDetailComponent implements OnInit {
  meetingDelivery?: IMeetingDelivery;
  receiverList?: IMeetingReceiver[] = [];
  attachmentList?: IMeetingAttachment[] = [];

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meeting }) => {
      this.meetingDelivery = meeting?.meetingDelivery;
      this.receiverList = meeting?.receiverList;
      this.attachmentList = meeting?.attachmentList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
