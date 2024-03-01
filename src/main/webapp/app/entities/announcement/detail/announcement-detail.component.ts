import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnouncement } from '../announcement.model';

@Component({
  selector: 'jhi-announcement-detail',
  templateUrl: './announcement-detail.component.html',
})
export class AnnouncementDetailComponent implements OnInit {
  announcement: IAnnouncement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ announcement }) => {
      this.announcement = announcement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
