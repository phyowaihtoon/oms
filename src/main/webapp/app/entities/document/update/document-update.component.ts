import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    docTitle: [],
  });

  constructor(protected fb: FormBuilder) {}

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
  }
}
