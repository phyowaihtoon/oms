import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentHeader } from '../document-header.model';

@Component({
  selector: 'jhi-document-header-detail',
  templateUrl: './document-header-detail.component.html',
})
export class DocumentHeaderDetailComponent implements OnInit {
  documentHeader: IDocumentHeader | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentHeader }) => {
      this.documentHeader = documentHeader;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
