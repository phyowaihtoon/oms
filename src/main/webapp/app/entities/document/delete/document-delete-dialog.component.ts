import { Component, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDocument } from '../document.model';

@Component({
  templateUrl: './document-delete-dialog.component.html',
})
export class DocumentDeleteDialogComponent {
  dmsDocument?: IDocument;

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
