import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-metadata-update',
  templateUrl: './metadata-update.component.html',
  styleUrls: ['./metadata-update.component.scss'],
})
export class MetadataUpdateComponent {
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
