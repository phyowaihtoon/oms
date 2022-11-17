import { Component, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-pdf-viewer',
  templateUrl: './pdf-viewer.component.html',
  styleUrls: ['./pdf-viewer.component.scss'],
})
export class PdfViewerComponent implements OnDestroy {
  pdfBlobURL: Blob = new Blob();

  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  ngOnDestroy(): void {
    this.pdfBlobURL = new Blob();
  }
}
