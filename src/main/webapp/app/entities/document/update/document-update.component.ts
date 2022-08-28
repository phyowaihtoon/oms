import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMetaDataHeader, MetaData, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { DocumentHeader, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  docTypes: MetaDataHeader[] | null = [];
  meta: MetaData[] | null = [];

  metaHeaderId: number = 0;

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
  });

  constructor(protected loadSetupService: LoadSetupService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  // ngOnInit(): void {
  //   this.activatedRoute.data.subscribe(({ category }) => {
  //     this.updateForm(category);
  //   });
  // }

  ngOnInit(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.docTypes = res.body;
        console.log(res);
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

    console.log(this.editForm.get(['id'])!.value);
    console.log(this.editForm.get(['metaDataHeaderId'])!.value);
    console.log(this.editForm.get(['fieldNames'])!.value);
    console.log(this.editForm.get(['fieldValues'])!.value);

    this.createFromForm();
  }

  onChange(e: any): void {
    this.metaHeaderId = e.target.value;

    console.log('Inside onChane....' + this.metaHeaderId.toString());

    this.loadSetupService.loadAllMetaDatabyMetadatHeaderId(this.metaHeaderId).subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.meta = res.body;
        console.log(res);
      },
      () => {
        console.log('error');
      }
    );
  }

  protected createFromForm(): IDocumentHeader {
    return {
      ...new DocumentHeader(),
      id: this.editForm.get(['id'])!.value,
      metaDataHeaderId: this.editForm.get(['metaDataHeaderId'])!.value,
      //fieldNames: this.editForm.get(['label'])!.value,
      //fieldValues:  this.editForm.get(['Text','Date'])!.value,
      //repositoryURL: "Testing URL"
    };
  }
}
