import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMetaDataHeader, MetaData, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { DocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  docTypes: MetaDataHeader[] | null = [];

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
  });

  constructor(protected documentService: DocumentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  // ngOnInit(): void {
  //   this.activatedRoute.data.subscribe(({ category }) => {
  //     this.updateForm(category);
  //   });
  // }

  ngOnInit(): void {
    this.documentService.getAllMetaDataHeader().subscribe((res: HttpResponse<IMetaDataHeader[]>) => {
      this.docTypes = res.body;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
  }

  onChanged(e: any): void {
    console.warn('Testing....');
  }

  protected updateForm(documentHeader: DocumentHeader): void {
    this.editForm.patchValue({});
  }
}
