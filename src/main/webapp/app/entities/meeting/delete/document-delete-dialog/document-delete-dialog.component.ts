import { Component, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IMeetingAttachment } from '../../meeting.model';

@Component({
  selector: 'jhi-document-delete-dialog',
  templateUrl: './document-delete-dialog.component.html',
  styleUrls: ['./document-delete-dialog.component.scss']
})
export class DocumentDeleteDialogComponent {

  dmsDocument?: IMeetingAttachment;

  @Output() confirmMessage: EventEmitter<any> = new EventEmitter();

  constructor(public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.confirmMessage.emit('YES');
    this.activeModal.close('deleted');
  }
}