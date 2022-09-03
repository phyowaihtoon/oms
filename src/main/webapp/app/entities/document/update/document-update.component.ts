import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMetaData, IMetaDataHeader, MetaData, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { stringify } from 'querystring';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DocumentHeader, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  docTypes: MetaDataHeader[] | null = [];
  metaData: IMetaData[] | null = [];

  metaHeaderId: number = 0;

  fNames: string[] = [];
  fName: string = '';

  fValues: string[] = [];
  fValue: string = '';

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],

    repositoryURL: [],
  });

  constructor(
    protected loadSetupService: LoadSetupService,
    protected documentHeaderService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  // ngOnInit(): void {
  //   this.activatedRoute.data.subscribe(({ category }) => {
  //     this.updateForm(category);
  //   });
  // }

  ngOnInit(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.docTypes = res.body;
        //  console.log(res);
      },
      () => {
        console.log('error');
      }
    );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    console.log('Inside SAVE....');
    this.isSaving = true;
    const documentHeaderdata = this.createFromForm();
    console.log(JSON.stringify(documentHeaderdata));
    if (documentHeaderdata.id !== undefined) {
      console.log('Inside Save ... () null');
      // this.subscribeToSaveResponse(this.documentHeaderService.update(metadata));
    } else {
      console.log('Inside Save ... ()');
      this.subscribeToSaveResponse(this.documentHeaderService.create(documentHeaderdata));
    }
  }

  onChange(e: any): void {
    this.metaHeaderId = e.target.value;
    this.loadSetupService.loadAllMetaDatabyMetadatHeaderId(this.metaHeaderId).subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.metaData = res.body;
        this.forControlBind();
      },
      () => {
        console.log('error');
      }
    );
  }

  forControlBind(): void {
    this.metaData?.forEach(metaDataItem => {
      const id: number = metaDataItem.id!;
      const idStr: string = id.toString();
      const fcnforFieldName: string = metaDataItem.fieldName! + '-' + idStr;

      this.editForm.addControl(fcnforFieldName + '-fieldName', this.fb.control([null]));
    });
  }

  getFieldName(group: FormGroup): string {
    this.fNames = [];
    this.fName = '';
    Object.keys(group.controls).forEach((key: string) => {
      const abstractControl = group.get(key);
      if (key.includes('-fieldName')) {
        const fieldName: string = key.split('-', 1).toString();
        this.fNames.push(fieldName);
      }
    });

    this.fNames.forEach(Name => {
      this.fName += Name;
      this.fName += '|';
    });
    return this.fName.slice(0, -1);
  }

  getFieldValue(group: FormGroup): string {
    this.fValues = [];
    this.fValue = '';
    Object.keys(group.controls).forEach((key: string) => {
      const abstractControl = group.get(key);
      if (key.includes('-fieldName')) {
        const fieldValue: string = abstractControl?.value;
        this.fValues.push(fieldValue);
      }
    });

    this.fValues.forEach(value => {
      this.fValue += value;
      this.fValue += '|';
    });
    return this.fValue.slice(0, -1);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHeader>>): void {
    console.log('Inside subscribeToSaveResponse');
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.editForm.reset();
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): IDocumentHeader {
    return {
      ...new DocumentHeader(),
      id: undefined,
      metaDataHeaderId: this.editForm.get(['metaDataHeaderId'])!.value,
      fieldValues: this.getFieldValue(this.editForm),
      fieldNames: this.getFieldName(this.editForm),
    };
  }
}
