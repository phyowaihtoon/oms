import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumentHeader, DocumentHeader } from '../document-header.model';
import { DocumentHeaderService } from '../service/document-header.service';

@Component({
  selector: 'jhi-document-header-update',
  templateUrl: './document-header-update.component.html',
})
export class DocumentHeaderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
  });

  constructor(
    protected documentHeaderService: DocumentHeaderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentHeader }) => {
      this.updateForm(documentHeader);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentHeader = this.createFromForm();
    if (documentHeader.id !== undefined) {
      this.subscribeToSaveResponse(this.documentHeaderService.update(documentHeader));
    } else {
      this.subscribeToSaveResponse(this.documentHeaderService.create(documentHeader));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHeader>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(documentHeader: IDocumentHeader): void {
    this.editForm.patchValue({
      id: documentHeader.id,
      metaDataHeaderId: documentHeader.metaDataHeaderId,
      fieldNames: documentHeader.fieldNames,
      fieldValues: documentHeader.fieldValues,
      repositoryURL: documentHeader.repositoryURL,
    });
  }

  protected createFromForm(): IDocumentHeader {
    return {
      ...new DocumentHeader(),
      id: this.editForm.get(['id'])!.value,
      metaDataHeaderId: this.editForm.get(['metaDataHeaderId'])!.value,
      fieldNames: this.editForm.get(['fieldNames'])!.value,
      fieldValues: this.editForm.get(['fieldValues'])!.value,
      repositoryURL: this.editForm.get(['repositoryURL'])!.value,
    };
  }
}
