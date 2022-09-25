import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMetaDataHeader, MetaDataHeader } from '../meta-data-header.model';
import { MetaDataHeaderService } from '../service/meta-data-header.service';

@Component({
  selector: 'jhi-meta-data-header-update',
  templateUrl: './meta-data-header-update.component.html',
})
export class MetaDataHeaderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    docTitle: [null, [Validators.required]],
  });

  constructor(
    protected metaDataHeaderService: MetaDataHeaderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaDataHeader }) => {
      this.updateForm(metaDataHeader);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metaDataHeader = this.createFromForm();
    if (metaDataHeader.id !== undefined) {
      this.subscribeToSaveResponse(this.metaDataHeaderService.update(metaDataHeader));
    } else {
      this.subscribeToSaveResponse(this.metaDataHeaderService.create(metaDataHeader));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaDataHeader>>): void {
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

  protected updateForm(metaDataHeader: IMetaDataHeader): void {
    this.editForm.patchValue({
      id: metaDataHeader.id,
      docTitle: metaDataHeader.docTitle,
    });
  }

  protected createFromForm(): IMetaDataHeader {
    return {
      ...new MetaDataHeader(),
      id: this.editForm.get(['id'])!.value,
      docTitle: this.editForm.get(['docTitle'])!.value,
    };
  }
}
