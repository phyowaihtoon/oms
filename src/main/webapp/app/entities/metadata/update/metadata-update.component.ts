import { Component, AfterViewInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators, FormControl } from '@angular/forms';
import { IMetaDataHeader, MetaDataHeader, IMetaData, MetaData } from '../metadata.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MetaDataService } from '../service/metadata.service';

@Component({
  selector: 'jhi-metadata-update',
  templateUrl: './metadata-update.component.html',
  styleUrls: ['./metadata-update.component.scss'],
})
export class MetadataUpdateComponent implements AfterViewInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    docTitle: ['', [Validators.required]],
    fieldList: this.fb.array([]),
  });

  fieldTyepList = [
    { value: 'String', caption: 'String' },
    { value: 'Number', caption: 'Number' },
    { value: 'Date', caption: 'Date' },
    { value: 'LOV', caption: 'LOV' },
  ];

  mendatoryList = [
    { value: 'YES', caption: 'YES' },
    { value: 'NO', caption: 'NO' },
  ];

  constructor(protected service: MetaDataService, protected fb: FormBuilder) {}

  /* ngOnInit(): void {

  } */

  ngAfterViewInit(): void {
    this.addField();
  }

  previousState(): void {
    window.history.back();
  }

  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      fieldName: ['', [Validators.required]],
      fieldType: ['', [Validators.required]],
      fieldValue: [{ value: '', disabled: true }, Validators.required],
      isRequired: ['', [Validators.required]],
      fieldOrder: ['', [Validators.required]],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    if (this.fieldList().length > 1) {
      this.fieldList().removeAt(i);
    }
  }

  removeAllField(): void {
    this.fieldList().clear();
  }

  onFieldTypeChange(i: any): void {
    if (this.fieldList().controls[i].get(['fieldType'])!.value === 'LOV') {
      this.fieldList().controls[i].get(['fieldValue'])!.enable({ onlySelf: true });
    } else {
      this.fieldList().controls[i].get(['fieldValue'])!.disable({ onlySelf: true });
    }
  }

  save(): void {
    this.isSaving = true;
    const metadata = this.createFromForm();
    if (metadata.id !== undefined) {
      this.subscribeToSaveResponse(this.service.update(metadata));
    } else {
      this.subscribeToSaveResponse(this.service.create(metadata));
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

  protected createMetaDataDetails(): IMetaData[] {
    const fieldList: IMetaData[] = [];
    this.fieldList().controls.forEach(data => {
      fieldList.push(this.createMetaDataDetail(data));
    });
    return fieldList;
  }

  protected createMetaDataDetail(data: any): IMetaData {
    return {
      ...new MetaData(),
      id: undefined,
      headerId: undefined,
      fieldName: data.get(['fieldName'])!.value,
      fieldType: data.get(['fieldType'])!.value,
      fieldValue: data.get(['fieldValue'])!.value,
      isRequired: data.get(['isRequired'])!.value,
      fieldOrder: data.get(['fieldOrder'])!.value,
    };
  }

  protected createFromForm(): IMetaDataHeader {
    return {
      ...new MetaDataHeader(),
      id: undefined,
      docTitle: this.editForm.get(['docTitle'])!.value,
      metaDataDetails: this.createMetaDataDetails(),
    };
  }
}
