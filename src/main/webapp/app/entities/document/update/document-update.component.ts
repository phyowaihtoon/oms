import { HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IMetaData, IMetaDataHeader, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Document, DocumentHeader, IDocument, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';
import { FileInfo } from 'app/entities/util/file-info.model';
import { RepositoryDialogComponent } from 'app/entities/util/repositorypopup/repository-dialog.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';

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
  _fileList: File[] = [];
  _tempFileList: File[] = [];
  repositoryurl: string = '';
  filenames: FileInfo[] = [];
  _saveButtonTitle: string = '';

  isDocMap = true;
  isUploadDetail = false;

  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;

  _modalRef?: NgbModalRef;

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
    this.loadSetupService.loadAllMetaDataHeader().subscribe((res: HttpResponse<IMetaDataHeader[]>) => {
      this.docTypes = res.body;

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
      this._fileList.splice(i, 1);
      this.myInputVariable!.nativeElement.value = '';
    }
  }

  // remove all Field of document table
  removeAllField(): void {
    this.docList1().clear();
  }

  showDocMap(): void {
    if (this.isDocMap === false) {
      this.isDocMap = !this.isDocMap;
    }

    this.isUploadDetail = false;
  }

  showUploadDetails(): void {
    if (this.isUploadDetail === false) {
      this.isUploadDetail = !this.isUploadDetail;
    }
    this.isDocMap = false;
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
    this.showLoading('Saving and Uploading Documents');

    const formData = new FormData();
    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    const documentHeaderdata = this.createFromForm();
    formData.append('documentHeaderData', JSON.stringify(documentHeaderdata));

    if (this._fileList.length > 0) {
      for (let i = 0; i < this._fileList.length; i++) {
        formData.append('files', this._fileList[i], this._fileList[i].name + '@' + this.getRepositoryURL(documentHeaderdata.docList, i));
      }
    }

    const docHeaderID = documentHeaderdata.id ?? undefined;
    if (docHeaderID !== undefined) {
      this.subscribeToSaveResponse(this.documentHeaderService.updateAndUploadDocuments(formData, docHeaderID));
    } else {
      this.subscribeToSaveResponse(this.documentHeaderService.createAndUploadDocuments(formData));
    }
  }

  // change event for doc template select box
  onDocTemplateChange(e: any): void {
    this.metaHeaderId = e.target.value;
    this.loadMetaDatabyMetadaHeaderID(this.metaHeaderId);
  }

  // load metadata by metadaheader ID
  loadMetaDatabyMetadaHeaderID(metaDataHeaderId: number): void {
    this.docTypes!.forEach((metaDataHeaderItem: IMetaDataHeader) => {
      if (metaDataHeaderId.toString() === metaDataHeaderItem.id?.toString()) {
        this.metaData = metaDataHeaderItem.metaDataDetails;
        this.forControlBind();
      }
    });
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

  showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  showLoading(loadingMessage?: string): void {
    this._modalRef = this.modalService.open(LoadingPopupComponent, { size: 'sm', backdrop: 'static', centered: true });
    this._modalRef.componentInstance.loadingMessage = loadingMessage;
  }

  hideLoading(): void {
    this._modalRef?.close();
  }

  checkRepositoryURL(inputFileElement: HTMLInputElement): void {
    if (
      this.editForm.get(['reposistory'])!.value === null ||
      this.editForm.get(['reposistory'])!.value === undefined ||
      this.editForm.get(['reposistory'])!.value.length === 0
    ) {
      const message1 = 'Please, select Repository URL first!';
      const message2 = 'Before choosing attached files, you need to select the repository location where your files will be uploaded.';
      this.showAlertMessage(message1, message2);
    } else {
      inputFileElement.click();
    }
  }

  // define a function to upload files
  onUploadFiles(event: Event): void {
    const target = event.target as HTMLInputElement;

    for (let i = 0; i <= target.files!.length - 1; i++) {
      const selectedFile = target.files![i];
      this._tempFileList.push(selectedFile);
    }

    this.repositoryurl = this.editForm.get(['reposistory'])!.value;

    for (let i = 0; i < this._tempFileList.length; i++) {
      this.addField(this.repositoryurl, this._tempFileList[i].name, Math.round(this._tempFileList[i].size / 1024));
      this._fileList.push(this._tempFileList[i]);
    }

    this._tempFileList = [];
    this.myInputVariable!.nativeElement.value = '';
  }

  getRepositoryURL(iDocument: IDocument[] | undefined, i: number): string {
    return iDocument![i].filePath!;
  }

  saveBtnTitleChange(): string {
    if (this.editForm.valid) {
      this._saveButtonTitle = '';
    } else {
      this._saveButtonTitle = 'Please fill all required fields.';
    }
    return this._saveButtonTitle;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReplyMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(result: HttpResponse<IReplyMessage>): void {
    const replyMessage: IReplyMessage | null = result.body;

    if (replyMessage !== null) {
      if (replyMessage.code === ResponseCode.SUCCESS_CODE) {
        // this._fileList = undefined;
        this._fileList = [];
        this.editForm.get(['id'])?.setValue(replyMessage.data.id);
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.showAlertMessage(replyCode, replyMsg);
      } else {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.showAlertMessage(replyCode, replyMsg);
      }
    } else {
      this.onSaveError();
    }
  }

  protected onSaveError(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
    this.hideLoading();
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

    this.docTypes!.forEach((metaDataHeaderItem: IMetaDataHeader) => {
      if (metaDataHeaderId.toString() === metaDataHeaderItem.id?.toString()) {
        this.metaDataUpdate = metaDataHeaderItem.metaDataDetails!;
      }

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
    });
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
      index = index + 1;
    });
  }
}
