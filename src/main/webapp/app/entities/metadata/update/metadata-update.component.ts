import { Component, AfterViewInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-metadata-update',
  templateUrl: './metadata-update.component.html',
  styleUrls: ['./metadata-update.component.scss'],
})
export class MetadataUpdateComponent implements AfterViewInit, OnDestroy {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    docTitle: [],
    fieldList: this.fb.array([]),
  });

  fieldTyepList = [
    { value: 'String', caption: 'String' },
    { value: 'Number', caption: 'Number' },
    { value: 'Date', caption: 'Date' },
    { value: 'LOV', caption: 'LOV' },
    { value: 'LOV2', caption: 'LOV2' },
    { value: 'LOV3', caption: 'LOV3' },
  ];

  mendatoryList = [
    { value: 'YES', caption: 'YES' },
    { value: 'NO', caption: 'NO' },
  ];

  constructor(protected fb: FormBuilder) {}

  /* ngOnInit(): void {

  } */

  ngAfterViewInit(): void {
    this.addField();
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
  }

  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      fieldName: ['', [Validators.required]],
      fieldType: ['', [Validators.required]],
      isRequired: ['', [Validators.required]],
      fieldOrder: ['', [Validators.required]],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    // if (this.fieldList().length !==1)
    this.fieldList().removeAt(i);
  }

  removeAllField(): void {
    this.fieldList().clear();
  }
}
