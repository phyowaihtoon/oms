import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentHeader } from '../document-header.model';
import { DocumentHeaderService } from '../service/document-header.service';

@Component({
  templateUrl: './document-header-delete-dialog.component.html',
})
export class DocumentHeaderDeleteDialogComponent {
  documentHeader?: IDocumentHeader;

  constructor(protected documentHeaderService: DocumentHeaderService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentHeaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
