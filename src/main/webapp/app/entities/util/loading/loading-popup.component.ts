import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-loading-popup',
  templateUrl: './loading-popup.component.html',
  styleUrls: ['./loading-popup.component.scss'],
})
export class LoadingPopupComponent {
  loadingMessage?: string = 'Loading....';

  constructor(public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }
}
