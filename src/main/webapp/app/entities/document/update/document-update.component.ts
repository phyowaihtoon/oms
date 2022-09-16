import { HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMetaData, IMetaDataHeader, MetaData, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { stringify } from 'querystring';

import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DocumentHeader, IDocument, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';
import { FileInfo } from 'app/entities/util/file-info.model';
import { version } from 'os';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  _documentHeader: IDocumentHeader | undefined;
  _documentDetails: IDocument[] | undefined;

  docTypes: MetaDataHeader[] | null = [];
  metaData: IMetaData[] | null = [];

  metaHeaderId: number = 0;

  fNames: string[] = [];
  fName: string = '';

  fValues: string[] = [];
  fValue: string = '';

  isSaving = false;

  // filenames: string[] = [];
  fileStatus = { status: '', requestType: '', percent: 0 };
  files?: FileList;
  repositoryurl: string = '';
  filenames: FileInfo[] = [];

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: ['', [Validators.required]],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
    message: ['', [Validators.required]],
    reposistory: ['', [Validators.required]],
    delFlag: [],
    docList: [],
    docList1: this.fb.array([]),
  });

  constructor(
    protected loadSetupService: LoadSetupService,
    protected documentHeaderService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // this.addField();

    this.activatedRoute.data.subscribe(({ docHeader }) => {
      this._documentHeader = docHeader;
      this._documentDetails = this._documentHeader?.docList;
      if (docHeader !== undefined) {
        this.updateForm(this._documentHeader!);
      }
    });

    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.docTypes = res.body;
        //  console.log(res);
      },
      () => {
        // console.log('error');
      }
    );
  }

  docList1(): FormArray {
    return this.editForm.get('docList1') as FormArray;
  }

  newField(fileName: string, fileSize: number): FormGroup {
    return this.fb.group({
      filePath: [fileName, [Validators.required]],
      fileSize: [fileSize, [Validators.required]],
      version: ['', [Validators.required]],
      remark: ['', [Validators.required]],
    });
  }

  addField(fileName: string, fileSize: number): void {
    this.docList1().push(this.newField(fileName, fileSize));
  }

  removeField(i: number): void {
    if (this.docList1().length > 0) {
      this.docList1().removeAt(i);
    }
  }

  removeAllField(): void {
    this.docList1().clear();
  }

  get metaDataHeaderId(): any {
    return this.editForm.get('metaDataHeaderId');
  }

  get message(): any {
    return this.editForm.get('message');
  }

  get reposistory(): any {
    return this.editForm.get('reposistory');
  }
  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentHeaderdata = this.createFromForm();
    this.onUploadFilestoPath();
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
    this.loadMetaDatabyMetadaHeaderID(this.metaHeaderId);
  }

  loadMetaDatabyMetadaHeaderID(metaDataHeaderId: number): void {
    this.loadSetupService.loadAllMetaDatabyMetadatHeaderId(metaDataHeaderId).subscribe(
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

      if (metaDataItem.isRequired === 'YES') {
        this.editForm.addControl(fcnforFieldName + '-fieldName', this.fb.control([null]));
      }
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

  // define a function to upload files
  onUploadFiles(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.files = target.files!;
    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    for (let i = 0; i < this.files.length; i++) {
      this.addField(this.repositoryurl + '/' + this.files.item(i)!.name, Math.round(this.files.item(i)!.size / 1024));
    }
    // this.forControlBindAttachment();
  }

  onUploadFilestoPath(): void {
    const formData = new FormData();
    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    for (let i = 0; i < this.files!.length; i++) {
      formData.append('files', this.files!.item(i)!, this.files!.item(i)!.name + '@' + this.repositoryurl);
    }
    this.documentHeaderService.upload(formData).subscribe(
      event => {
        console.log(event);
        this.resportProgress(event);
      },
      (error: HttpErrorResponse) => {
        console.log(error);
      }
    );
  }

  updateStatus(loaded: number, total: number, resquestType: string): void {
    this.fileStatus.status = 'progress';
    this.fileStatus.requestType = resquestType;
    this.fileStatus.percent = Math.round((100 * loaded) / total);
  }

  // forControlBindAttachment(): void {
  //   console.log(this.filenames);
  //   console.log(this.filenames.length);

  //   for (let i = 0; i < this.filenames.length; i++) {
  //     console.log('inside forControlBindAttachment');
  //     const number = i.toString();
  //     this.editForm.addControl('Remark' + number, this.fb.control([null]));
  //     this.editForm.addControl('Version' + number, this.fb.control([null]));
  //     this.editForm.addControl('Filesize' + number, this.fb.control([null]));
  //   }
  // }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHeader>>): void {
    console.log('Inside subscribeToSaveResponse');
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.editForm.reset();
    this.filenames = [];
  }

  protected onSaveError(): void {
    console.log('error');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): IDocumentHeader {
    return {
      ...new DocumentHeader(),
      id: undefined,
      metaDataHeaderId: this.editForm.get(['metaDataHeaderId'])!.value,
      message: this.editForm.get(['message'])!.value,
      fieldValues: this.getFieldValue(this.editForm),
      fieldNames: this.getFieldName(this.editForm),
      delFlag: 'N',
      repositoryURL: '',
      docList: this.createFileListDetails(),
    };
  }

  protected createFileListDetails(): IDocument[] {
    const fieldList: IDocument[] = [];
    this.docList1().controls.forEach(data => {
      fieldList.push(this.createFileListDetail(data));
    });
    return fieldList;
  }

  protected createFileListDetail(data: any): IDocument {
    const reposistory = this.editForm.get(['reposistory'])!.value;
    return {
      ...new Document(),
      id: undefined,
      headerId: undefined,
      filePath: data.get(['filePath'])!.value,
      fileSize: data.get(['fileSize'])!.value,
      version: data.get(['version'])!.value,
      remark: data.get(['remark'])!.value,
      delFlag: 'N',
    };
  }

  protected updateForm(docHeaderData: IDocumentHeader): void {
    this.loadMetaDatabyMetadaHeaderID(docHeaderData.metaDataHeaderId!);

    this.editForm.patchValue({
      id: docHeaderData.id,
      metaDataHeaderId: docHeaderData.metaDataHeaderId,
      message: docHeaderData.message,
      docList: this.updateDocumentDataDetails(docHeaderData.docList!),
    });
  }

  protected updateDocumentDataDetails(docList: IDocument[]): void {
    console.log(docList);
  }

  private resportProgress(httpEvent: HttpEvent<string[] | Blob>): void {
    switch (httpEvent.type) {
      case HttpEventType.UploadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Uploading');
        break;
      case HttpEventType.DownloadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Downloading');
        break;
      case HttpEventType.ResponseHeader:
        console.log('Header returned', httpEvent);
        break;
      case HttpEventType.Response:
        if (httpEvent.body instanceof Array) {
          for (const filename of httpEvent.body) {
            // this.filenames.unshift(filename);
          }
        } else {
          // download logic
          saveAs(
            new File([httpEvent.body!], httpEvent.headers.get('File-Name')!, {
              type: `${httpEvent.headers.get('Content-Type')!};charset=utf-8`,
            })
          );
        }
        this.fileStatus.status = 'done';
        break;
      default:
        console.log(httpEvent);
        break;
    }
  }
}
