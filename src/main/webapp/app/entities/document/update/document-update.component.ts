import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { MetaData } from 'app/entities/metadata/metadata.model';
import { DocumentHeader } from '../document.model';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent {
  docTypes: MetaData[] = [];

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
  });

  constructor(protected fb: FormBuilder) {}

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
