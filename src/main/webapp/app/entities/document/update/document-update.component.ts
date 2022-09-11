import { HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMetaData, IMetaDataHeader, MetaData, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { stringify } from 'querystring';

import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DocumentHeader, IDocument, IDocumentHeader } from '../document.model';
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

  filenames: string[] = [];
  fileStatus = { status: '', requestType: '', percent: 0 };
  files?: FileList;
  repositoryurl: string = '/FTP_Folder/Test/Test1/';

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: [],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
    message: [],
    delFlag: [],
    docList: [],
  });

  constructor(
    protected loadSetupService: LoadSetupService,
    protected documentHeaderService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

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

  // define a function to upload files
  onUploadFiles(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.files = target.files!;

    const formData = new FormData();
    for (let i = 0; i < this.files.length; i++) {
      //formData.append('files', this.files.item(i)!, this.files.item(i)!.name+"@"+this.repositoryurl);
      this.filenames.unshift(this.files.item(i)!.name);
    }

    this.forControlBindAttachment();
  }

  onUploadFilestoPath(): void {
    const formData = new FormData();
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

  forControlBindAttachment(): void {
    console.log(this.filenames);
    console.log(this.filenames.length);

    for (let i = 0; i < this.filenames.length; i++) {
      console.log('inside forControlBindAttachment');
      const number = i.toString();
      this.editForm.addControl('Remark' + number, this.fb.control([null]));
      this.editForm.addControl('Version' + number, this.fb.control([null]));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHeader>>): void {
    console.log('Inside subscribeToSaveResponse');
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.onUploadFilestoPath();
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
      repositoryURL: '/FTP_Folder/Test/Test1/',
      docList: this.createFileListDetails(),
    };
  }

  protected createFileListDetails(): IDocument[] {
    const fieldList: IDocument[] = [];
    for (let i = 0; i < this.filenames.length; i++) {
      fieldList.push(this.createFileListDetail(this.filenames[i], i));
    }
    return fieldList;
  }

  protected createFileListDetail(data: string, number: number): IDocument {
    const fieldNoString: string = number.toString();
    const remarkFieldName: string = 'Remark' + fieldNoString;
    const versionFieldName: string = 'Version' + fieldNoString;
    return {
      ...new Document(),
      id: undefined,
      headerId: undefined,
      filePath: data,
      fileSize: undefined,
      version: this.editForm.get([versionFieldName])!.value,
      remark: this.editForm.get([remarkFieldName])!.value,
      delFlag: 'N',
    };
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
