import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentHeader } from '../document-header.model';
import { DocumentHeaderService } from '../service/document-header.service';
import { DocumentHeaderDeleteDialogComponent } from '../delete/document-header-delete-dialog.component';

@Component({
  selector: 'jhi-document-header',
  templateUrl: './document-header.component.html',
})
export class DocumentHeaderComponent implements OnInit {
  documentHeaders?: IDocumentHeader[];
  isLoading = false;

  constructor(protected documentHeaderService: DocumentHeaderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentHeaderService.query().subscribe(
      (res: HttpResponse<IDocumentHeader[]>) => {
        this.isLoading = false;
        this.documentHeaders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentHeader): number {
    return item.id!;
  }

  delete(documentHeader: IDocumentHeader): void {
    const modalRef = this.modalService.open(DocumentHeaderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentHeader = documentHeader;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
