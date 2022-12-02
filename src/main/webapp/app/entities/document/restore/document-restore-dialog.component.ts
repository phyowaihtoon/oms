import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDocument, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';

@Component({
  selector: 'jhi-document-restore-dialog',
  templateUrl: './document-restore-dialog.component.html',
  styleUrls: ['./document-restore-dialog.component.scss'],
})
export class DocumentRestoreDialogComponent {
  documentHeader?: IDocumentHeader;

  constructor(protected documentService: DocumentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmRestore(id: number): void {
    this.documentService.restoreDocument(id).subscribe(() => {
      this.activeModal.close('restored');
    });
  }
}
