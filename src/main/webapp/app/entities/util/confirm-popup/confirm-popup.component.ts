import { Component, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-confirm-popup',
  templateUrl: './confirm-popup.component.html',
  styleUrls: ['./confirm-popup.component.scss'],
})
export class ConfirmPopupComponent {
  confirmTitle?: string = 'CONFIRMATION';

  @Output() actionMessage: EventEmitter<any> = new EventEmitter();

  constructor(public activeModal: NgbActiveModal) {}

  confirm(): void {
    this.actionMessage.emit('CONFIRM');
    this.activeModal.dismiss();
  }

  cancel(): void {
    this.actionMessage.emit('CANCEL');
    this.activeModal.dismiss();
  }
}
