import { HttpErrorResponse, HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IMetaData, IMetaDataHeader, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DocumentHeader, IDocument, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';
import { FileInfo } from 'app/entities/util/file-info.model';
import { RepositoryDialogComponent } from 'app/entities/util/repositorypopup/repository-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  _documentHeader: IDocumentHeader | undefined;
  _documentDetails: IDocument[] | undefined;

  docTypes: MetaDataHeader[] | null = [];
  _fieldValue?: string[] = [];
  metaData?: IMetaData[];
  metaDataUpdate: IMetaData[] | null = [];

  metaHeaderId: number = 0;

  fNames: string[] = [];
  fName: string = '';

  fValues: string[] = [];
  fValue: string = '';

  isSaving = false;

  // filenames: string[] = [];
  fileStatus = { status: '', requestType: '', percent: 0 };
  _fileList: FileList | undefined;
  repositoryurl: string = '';
  filenames: FileInfo[] = [];

  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: ['', [Validators.required]],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
    message: [],
    reposistory: [],
    delFlag: [],
    docList: [],
    docList1: this.fb.array([]),
  });

  constructor(
    protected loadSetupService: LoadSetupService,
    protected documentHeaderService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ docHeader }) => {
      this._documentHeader = docHeader;
      this._documentDetails = this._documentHeader?.docList;
      if (this._documentHeader !== undefined) {
        if (this._documentHeader.id !== undefined && docHeader.id !== null) {
          this.removeAllField();
        }
        this.updateForm(docHeader);
      }
    });

    this.loadSetupService.loadAllMetaDataHeader().subscribe((res: HttpResponse<IMetaDataHeader[]>) => {
      this.docTypes = res.body;
    });
  }

  // define FormArray for document List
  docList1(): FormArray {
    return this.editForm.get('docList1') as FormArray;
  }

  // create new field dynamically
  newField(filePath: string, fileName: string, fileSize: number): FormGroup {
    return this.fb.group({
      filePath: [filePath, [Validators.required]],
      fileName: [fileName, [Validators.required]],
      fileSize: [fileSize, [Validators.required]],
      version: ['', [Validators.required]],
      remark: [''],
    });
  }

  // add new field dynamically
  addField(filePath: string, fileName: string, fileSize: number): void {
    this.docList1().push(this.newField(filePath, fileName, fileSize));
  }

  // remove field by given row id
  removeField(i: number): void {
    if (this.docList1().length > 0) {
      this.docList1().removeAt(i);
      this.myInputVariable!.nativeElement.value = '';
    }
  }

  // remove all Field of document table
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

  // window.history.back();
  previousState(): void {
    this.editForm.controls['id']!.setValue(undefined);
    this.editForm.controls['metaDataHeaderId']!.setValue('');
    this.editForm.controls['message']!.setValue('');
    this.editForm.controls['reposistory']!.setValue('');
    this.removeAllField();
    this.loadMetaDatabyMetadaHeaderID(0);
  }

  searchRepository(): void {
    const modalRef = this.modalService.open(RepositoryDialogComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.passEntry.subscribe((data: any) => {
      this.editForm.get(['reposistory'])!.setValue(data);
    });
  }

  // save the form here
  save(): void {
    this.isSaving = true;

    if (this._fileList !== undefined) {
      this.onUploadFilestoPath();
    } else {
      const documentHeaderdata = this.createFromForm();
      console.log(JSON.stringify(documentHeaderdata));
      if (documentHeaderdata.id! > 0) {
        this.subscribeToSaveResponse(this.documentHeaderService.update(documentHeaderdata));
      } else {
        this.subscribeToSaveResponse(this.documentHeaderService.create(documentHeaderdata));
      }
    }
  }

  // change event for doc template select box
  onChange(e: any): void {
    this.metaHeaderId = e.target.value;
    this.loadMetaDatabyMetadaHeaderID(this.metaHeaderId);
  }

  // load metadata by metadaheader ID
  loadMetaDatabyMetadaHeaderID(metaDataHeaderId: number): void {
    this.loadSetupService.loadAllMetaDatabyMetadatHeaderId(metaDataHeaderId).subscribe(
      (res: HttpResponse<IMetaData[]>) => {
        if (res.body) {
          this.metaData = res.body;
        }
        this.forControlBind();
      },
      () => {
        console.log('error');
      }
    );
  }

  forControlBind(): void {
    Object.keys(this.editForm.controls).forEach((key: string) => {
      const abstractControl = this.editForm.get(key);
      if (key.includes('_fieldName')) {
        const fieldName: string = key;
        this.editForm.removeControl(fieldName);
      }
    });

    this.metaData?.forEach((metaDataItem: IMetaData) => {
      const id: number = metaDataItem.id!;
      const idStr: string = id.toString();
      const fcnforFieldName: string = metaDataItem.fieldName! + '_' + idStr;

      if (metaDataItem.isRequired === 'YES') {
        this.editForm.addControl(fcnforFieldName + '_fieldName', new FormControl('', Validators.required));
      } else {
        this.editForm.addControl(fcnforFieldName + '_fieldName', new FormControl(''));
      }

      // if (metaDataItem.fieldValue !== '') {
      //   this._fieldValue = metaDataItem.fieldValue?.split('|');
      // }
    });
  }

  getFieldValues(fieldValue?: string): string[] {
    if (fieldValue !== undefined) {
      return fieldValue.split('|');
    }
    return [];
  }

  getFieldName(group: FormGroup): string {
    this.fNames = [];
    this.fName = '';

    Object.keys(group.controls).forEach((key: string) => {
      const abstractControl = group.get(key);
      if (key.includes('_fieldName')) {
        const fieldName: string = key.split('_', 1).toString();
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
      if (key.includes('_fieldName')) {
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

  showInfoMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.messageLine1 = msg1;
    modalRef.componentInstance.messageLine2 = msg2;
  }

  checkRepositoryURL(inputFileElement: HTMLInputElement): void {
    if (
      this.editForm.get(['reposistory'])!.value === null ||
      this.editForm.get(['reposistory'])!.value === undefined ||
      this.editForm.get(['reposistory'])!.value.length === 0
    ) {
      const message1 = 'Please, select repository URL (File Directory) first!';
      const message2 = 'Before choosing attached files, you need to select the repository location where your files will be uploaded.';
      this.showInfoMessage(message1, message2);
    } else {
      inputFileElement.click();
    }
  }

  // define a function to upload files
  onUploadFiles(event: Event): void {
    const target = event.target as HTMLInputElement;
    this._fileList = target.files!;
    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    for (let i = 0; i < this._fileList.length; i++) {
      this.addField(this.repositoryurl, this._fileList.item(i)!.name, Math.round(this._fileList.item(i)!.size / 1024));
    }
  }

  onUploadFilestoPath(): void {
    const formData = new FormData();
    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    for (let i = 0; i < this._fileList!.length; i++) {
      formData.append('files', this._fileList!.item(i)!, this._fileList!.item(i)!.name + '@' + this.repositoryurl);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHeader>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(result: HttpResponse<IDocumentHeader>): void {
    this.editForm.get(['id'])?.setValue(result.body?.id);
    const message1 = 'Document Mapping is saved successfully';
    this.showInfoMessage(message1);
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
      id: this.editForm.get(['id'])!.value,
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
      fileName: data.get(['fileName'])!.value,
      fileSize: data.get(['fileSize'])!.value,
      version: data.get(['version'])!.value,
      remark: data.get(['remark'])!.value,
      delFlag: 'N',
    };
  }

  protected updateForm(docHeaderData: IDocumentHeader): void {
    this.loadMetaDatabyMetadaHeaderID(docHeaderData.metaDataHeaderId!);
    this.updateDynamicField(docHeaderData.metaDataHeaderId!, docHeaderData.fieldValues!, docHeaderData.fieldNames!);

    this.editForm.patchValue({
      id: docHeaderData.id,
      metaDataHeaderId: docHeaderData.metaDataHeaderId,
      message: docHeaderData.message,
      fieldValues: docHeaderData.fieldValues,
      fieldNames: docHeaderData.fieldNames,
      docList: this.updateDocumentDataDetails(docHeaderData.docList),
    });
  }

  protected updateDynamicField(metaDataHeaderId: number, fieldValue: string, fieldName: string): void {
    const fn = fieldName.split('|');
    const fv = fieldValue.split('|');

    this.loadSetupService.loadAllMetaDatabyMetadatHeaderId(metaDataHeaderId).subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.metaDataUpdate = res.body;

        this.metaDataUpdate?.forEach(metaDataItem => {
          const id: number = metaDataItem.id!;
          const idStr: string = id.toString();
          const fcnforFieldName: string = metaDataItem.fieldName! + '_' + idStr + '_fieldName';

          for (let i = 0; i < fn.length; i++) {
            if (fn[i].includes(metaDataItem.fieldName!)) {
              this.editForm.get([fcnforFieldName])!.setValue(fv[i]);
            }
          }
        });
      },
      () => {
        console.log('error');
      }
    );
  }

  protected updateDocumentDataDetails(docList: IDocument[] | undefined): void {
    let index = 0;
    docList?.forEach(data => {
      this.addField('', '', 0);
      this.docList1().controls[index].get(['filePath'])!.setValue(data.filePath);
      this.docList1().controls[index].get(['fileName'])!.setValue(data.fileName);
      this.docList1().controls[index].get(['fileSize'])!.setValue(data.fileSize);
      this.docList1().controls[index].get(['remark'])!.setValue(data.remark);
      this.docList1().controls[index].get(['version'])!.setValue(data.version);
      // this.onFieldTypeChange(index);
      index = index + 1;
    });
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

        {
          this.fileStatus.status = 'done';
          this._fileList = undefined;
          const documentHeaderdata = this.createFromForm();
          console.log(JSON.stringify(documentHeaderdata));
          if (documentHeaderdata.id! > 0) {
            this.subscribeToSaveResponse(this.documentHeaderService.update(documentHeaderdata));
          } else {
            this.subscribeToSaveResponse(this.documentHeaderService.create(documentHeaderdata));
          }
        }

        break;
      default: {
        console.log(httpEvent);
        break;
      }
    }
  }
}
