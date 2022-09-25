import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMetaData, MetaData } from '../meta-data.model';
import { MetaDataService } from '../service/meta-data.service';

@Component({
  selector: 'jhi-meta-data-update',
  templateUrl: './meta-data-update.component.html',
})
export class MetaDataUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    headerId: [null, [Validators.required]],
    fieldName: [null, [Validators.required]],
    fieldType: [null, [Validators.required]],
    isRequired: [null, [Validators.required]],
    fieldOrder: [],
    fieldValue: [],
  });

  constructor(protected metaDataService: MetaDataService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaData }) => {
      this.updateForm(metaData);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metaData = this.createFromForm();
    if (metaData.id !== undefined) {
      this.subscribeToSaveResponse(this.metaDataService.update(metaData));
    } else {
      this.subscribeToSaveResponse(this.metaDataService.create(metaData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaData>>): void {
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

  protected updateForm(metaData: IMetaData): void {
    this.editForm.patchValue({
      id: metaData.id,
      headerId: metaData.headerId,
      fieldName: metaData.fieldName,
      fieldType: metaData.fieldType,
      isRequired: metaData.isRequired,
      fieldOrder: metaData.fieldOrder,
      fieldValue: metaData.fieldValue,
    });
  }

  protected createFromForm(): IMetaData {
    return {
      ...new MetaData(),
      id: this.editForm.get(['id'])!.value,
      headerId: this.editForm.get(['headerId'])!.value,
      fieldName: this.editForm.get(['fieldName'])!.value,
      fieldType: this.editForm.get(['fieldType'])!.value,
      isRequired: this.editForm.get(['isRequired'])!.value,
      fieldOrder: this.editForm.get(['fieldOrder'])!.value,
      fieldValue: this.editForm.get(['fieldValue'])!.value,
    };
  }
}
