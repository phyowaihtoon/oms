import { Component, AfterViewInit, OnInit } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators, FormControl } from '@angular/forms';
import { IMetaDataHeader, MetaDataHeader, IMetaData, MetaData } from '../metadata.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MetaDataService } from '../service/metadata.service';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LovSetupDialogComponent } from '../lov-setup/lov-setup-dialog.component';

@Component({
  selector: 'jhi-metadata-update',
  templateUrl: './metadata-update.component.html',
  styleUrls: ['./metadata-update.component.scss'],
})
export class MetadataUpdateComponent implements OnInit {
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

  constructor(
    protected service: MetaDataService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.addField();
    this.activatedRoute.data.subscribe(({ metadata }) => {
      if (metadata !== null) {
        if (metadata.id !== null && metadata.id !== undefined) {
          this.removeAllField();
        }
        this.updateForm(metadata);
      }
    });
  }

  cancel(): void {
    // window.history.back();
    this.editForm.controls['id']!.setValue(undefined);
    this.editForm.controls['docTitle']!.setValue('');
    this.removeAllField();
    this.addField();
  }

  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      fieldName: ['', [Validators.required]],
      fieldType: ['String', [Validators.required]],
      fieldValue: [{ value: '', disabled: true }, Validators.required],
      isRequired: ['YES', [Validators.required]],
      fieldOrder: [this.fieldList().controls.length + 1, [Validators.required]],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    if (this.fieldList().length > 1) {
      this.fieldList().removeAt(i);
      this.reorderFieldList();
    }
  }

  reorderFieldList(): void {
    let index = 1;
    this.fieldList().controls.forEach(data => {
      data.get(['fieldOrder'])!.setValue(index);
      index = index + 1;
    });
  }

  removeAllField(): void {
    this.fieldList().clear();
  }

  onFieldTypeChange(i: any): void {
    if (this.fieldList().controls[i].get(['fieldType'])!.value === 'LOV') {
      this.fieldList().controls[i].get(['fieldValue'])!.enable({ onlySelf: true });
    } else {
      this.fieldList().controls[i].get(['fieldValue'])!.setValue('');
      this.fieldList().controls[i].get(['fieldValue'])!.disable({ onlySelf: true });
    }
  }

  addLovValue(i: number): void {
    const val = this.fieldList().controls[i].get(['fieldValue'])!.value;
    // this.fieldList().push(this.newField());
    const modalRef = this.modalService.open(LovSetupDialogComponent, { size: 'md', backdrop: 'static' });
    modalRef.componentInstance.lovStr = this.fieldList().controls[i].get(['fieldValue'])!.value;
    modalRef.componentInstance.fieldName = this.fieldList().controls[i].get(['fieldName'])!.value;
    // unsubscribe not needed because closed completes on modal close
    modalRef.componentInstance.passEntry.subscribe((data: any) => {
      this.fieldList().controls[i].get(['fieldValue'])!.setValue(data);
    });
  }

  save(): void {
    this.isSaving = true;
    const metadata = this.createForm();
    console.log(JSON.stringify(metadata));
    if (metadata.id !== undefined) {
      this.subscribeToSaveResponse(this.service.update(metadata));
    } else {
      this.subscribeToSaveResponse(this.service.create(metadata));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaDataHeader>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(result: HttpResponse<IMetaDataHeader>): void {
    this.editForm.get(['id'])?.setValue(result.body?.id);
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
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
      delFlag: 'N',
    };
  }

  protected createMetaDataDetails(): IMetaData[] {
    const fieldList: IMetaData[] = [];
    this.fieldList().controls.forEach(data => {
      fieldList.push(this.createMetaDataDetail(data));
    });
    return fieldList;
  }

  protected createForm(): IMetaDataHeader {
    return {
      ...new MetaDataHeader(),
      id: this.editForm.get(['id'])!.value,
      docTitle: this.editForm.get(['docTitle'])!.value,
      delFlag: 'N',
      metaDataDetails: this.createMetaDataDetails(),
    };
  }

  protected updateForm(metadata: IMetaDataHeader): void {
    this.editForm.patchValue({
      id: metadata.id,
      docTitle: metadata.docTitle,
      fieldList: this.updateMetaDataDetails(metadata.metaDataDetails),
    });
  }

  protected updateMetaDataDetails(metaDataDetails: IMetaData[] | undefined): void {
    let index = 0;
    metaDataDetails?.forEach(data => {
      this.addField();
      this.fieldList().controls[index].get(['fieldName'])!.setValue(data.fieldName);
      this.fieldList().controls[index].get(['fieldType'])!.setValue(data.fieldType);
      this.fieldList().controls[index].get(['fieldValue'])!.setValue(data.fieldValue);
      this.fieldList().controls[index].get(['isRequired'])!.setValue(data.isRequired);
      this.fieldList().controls[index].get(['fieldOrder'])!.setValue(data.fieldOrder);
      this.onFieldTypeChange(index);
      index = index + 1;
    });
  }
}
