import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IMetaData, IMetaDataHeader, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DMSDocument, DocumentHeader, DocumentInquiry, IDocument, IDocumentHeader } from '../document.model';
import { DocumentService } from '../service/document.service';
import { FileInfo } from 'app/entities/util/file-info.model';
import { RepositoryDialogComponent } from 'app/entities/util/repositorypopup/repository-dialog.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { IDocumentStatus, IMenuItem, IPriority } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { ValueConverter } from '@angular/compiler/src/render3/view/template';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  styleUrls: ['./document-update.component.scss'],
})
export class DocumentUpdateComponent implements OnInit {
  _documentHeader: IDocumentHeader | undefined;
  _documentDetails: IDocument[] | undefined;
  _documentStatus?: IDocumentStatus[];

  _metaDataHdrList: MetaDataHeader[] | null = [];
  _priority: IPriority[] | null = [];
  _fieldValue?: string[] = [];
  metaData?: IMetaData[];
  metaDataUpdate: IMetaData[] | null = [];

  metaHeaderId: number = 0;

  fNames: string[] = [];
  fName: string = '';

  fValues: string[] = [];
  fValue: string = '';

  isSaving = false;
  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;

  // filenames: string[] = [];
  fileStatus = { status: '', requestType: '', percent: 0 };
  _tempFileList: File[] = [];
  repositoryurl: string = '';
  filenames: FileInfo[] = [];
  _saveButtonTitle: string = '';

  isDocMap = true;
  isUploadDetail = false;

  isNA = true;
  isSentApprove = false;
  isCancel = false;
  docStatus: string = '';
  docStatus_number: number = 0;

  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;

  _modalRef?: NgbModalRef;

  editForm = this.fb.group({
    id: [],
    metaDataHeaderId: ['', [Validators.required]],
    fieldNames: [],
    fieldValues: [],
    repositoryURL: [],
    message: [],
    priority: [],
    status: [1],
    reposistory: [],
    reject: [],
    ammendment: [],
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
    this.loadAllSetup();
    this.activatedRoute.data.subscribe(({ docHeader, userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;
      this._metaDataHdrList = userAuthority.templateList;

      this._documentHeader = docHeader;
      this._documentDetails = this._documentHeader?.docList;

      if (this._documentHeader !== undefined) {
        if (this._documentHeader.id !== undefined && docHeader.id !== null) {
          this.removeAllField();
        }
        this.updateForm(docHeader);
      }
    });
  }

  loadAllSetup(): void {
    this.loadSetupService.loadPriority().subscribe((res_priority: HttpResponse<IPriority[]>) => {
      this._priority = res_priority.body;
    });

    this.loadSetupService.loadDocumentStatus().subscribe(
      (res_status: HttpResponse<IDocumentStatus[]>) => {
        if (res_status.body) {
          this._documentStatus = res_status.body;
        }
      },
      error => {
        console.log('Document Status Failed : ', error);
      }
    );
  }

  // define FormArray for document List
  docList1(): FormArray {
    return this.editForm.get('docList1') as FormArray;
  }

  // create new field dynamically
  newField(filePath: string, fileName: string, fileSize: number, fileData?: File): FormGroup {
    return this.fb.group({
      id: [],
      filePath: [filePath, [Validators.required]],
      fileName: [fileName, [Validators.required]],
      fileNameVersion: [],
      fileSize: [fileSize, [Validators.required]],
      version: [''],
      remark: [''],
      fileData: [fileData],
    });
  }

  // add new field dynamically
  addField(filePath: string, fileName: string, fileSize: number, fileData?: File): void {
    this.docList1().push(this.newField(filePath, fileName, fileSize, fileData));
  }

  // remove field by given row id
  removeField(i: number): void {
    if (this.docList1().length > 0) {
      const id = this.docList1().controls[i].get(['id'])!.value;

      if (this.docList1().controls[i].get(['id'])!.value === null || this.docList1().controls[i].get(['id'])!.value === undefined) {
        this.removeFieldConfirm(i);
      } else {
        this.subscribeToSaveResponseCheckFileexist(this.documentHeaderService.deleteFile(id), i);
      }
    }
  }

  removeFieldConfirm(i: number): void {
    this.docList1().removeAt(i);
    this.myInputVariable!.nativeElement.value = '';
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

  // window.history.back();
  previousState(): void {
    this.editForm.controls['id']!.setValue(undefined);
    this.editForm.controls['metaDataHeaderId']!.setValue('');
    this.editForm.controls['priority']!.setValue('');
    this.editForm.controls['message']!.setValue('');
    this.editForm.controls['reject']!.setValue('');
    this.editForm.controls['ammendment']!.setValue('');
    this.editForm.controls['reposistory']!.setValue('');
    this.removeAllField();
    this.loadMetaDatabyMetadaHeaderID(0);
    this.isDocMap = true;
    this.isUploadDetail = false;
    this.isNA = true;
    this.isSentApprove = false;
    this.isCancel = false;
    this.docStatus = '';
    this.docStatus_number = 0;
  }

  searchRepository(): void {
    const modalRef = this.modalService.open(RepositoryDialogComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.passEntry.subscribe((data: any) => {
      this.editForm.get(['reposistory'])!.setValue(data);
    });
  }

  // save the form here
  save(status: number): void {
    this.isSaving = true;
    this.showLoading('Saving and Uploading Documents');

    const formData = new FormData();
    const attachedFileList = [];

    const documentHeaderdata = this.createFromForm(status);
    const docList = documentHeaderdata.docList ?? [];

    if (docList.length > 0) {
      for (const dmsDoc of docList) {
        const docDetailID = dmsDoc.id ?? undefined;
        if (docDetailID === undefined && dmsDoc.fileData !== undefined) {
          const orgFileName = dmsDoc.fileData.name;
          const orgFileExtension = orgFileName.split('.').pop();
          let editedFileName = dmsDoc.fileName;
          if (editedFileName?.lastIndexOf('.') === -1) {
            // If file extension is missing, original file extension is added
            editedFileName = editedFileName.concat('.').concat(orgFileExtension!);
            dmsDoc.fileName = editedFileName;
          }
          formData.append('files', dmsDoc.fileData, editedFileName?.concat('@').concat(dmsDoc.filePath ?? ''));
        }
        delete dmsDoc['fileData'];
        attachedFileList.push(dmsDoc);
      }
    }

    documentHeaderdata.docList = attachedFileList;
    formData.append('documentHeaderData', JSON.stringify(documentHeaderdata));
    // formData.append('paraStatus', status.toString());

    const docHeaderID = documentHeaderdata.id ?? undefined;
    if (docHeaderID !== undefined) {
      this.subscribeToSaveResponse(this.documentHeaderService.updateAndUploadDocuments(formData, docHeaderID));
    } else {
      this.subscribeToSaveResponse(this.documentHeaderService.createAndUploadDocuments(formData));
    }
  }

  updateStatus(status: number): void {
    this.docStatus_number = status;
    const docHeaderId = this.editForm.get(['id'])!.value;

    const approvalInfo = {
      ...new DocumentInquiry(),
      status: this.docStatus_number,
      approvedBy: this._userAuthority!.userID,
    };
    this.showLoading('Approving in progress');
    this.subscribeTeUpdateStatusResponse(this.documentHeaderService.updateDocumentStatus(approvalInfo, docHeaderId));
  }

  // change event for doc template select box
  onDocTemplateChange(e: any): void {
    this.metaHeaderId = e.target.value;
    this.loadMetaDatabyMetadaHeaderID(this.metaHeaderId);
  }

  // load metadata by metadaheader ID
  loadMetaDatabyMetadaHeaderID(metaDataHeaderId: number): void {
    if (metaDataHeaderId === 0) {
      this.metaData = [];
      this.forControlBind();
    } else {
      this._metaDataHdrList!.forEach((metaDataHeaderItem: IMetaDataHeader) => {
        if (metaDataHeaderId.toString() === metaDataHeaderItem.id?.toString()) {
          this.metaData = metaDataHeaderItem.metaDataDetails;
          this.forControlBind();
        }
      });
    }
  }

  forControlBind(): void {
    Object.keys(this.editForm.controls).forEach((key: string) => {
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
      const tempFile = this._tempFileList[i];
      this.addField(this.repositoryurl, tempFile.name, Math.round(tempFile.size / 1024), tempFile);
    }

    this._tempFileList = [];
    this.myInputVariable!.nativeElement.value = '';
  }

  saveBtnTitleChange(): string {
    if (this.editForm.valid) {
      this._saveButtonTitle = '';
    } else {
      this._saveButtonTitle = 'Please fill all required fields.';
    }
    return this._saveButtonTitle;
  }

  getDocumentStatus(id?: number): string | undefined {
    const docStatus = this._documentStatus?.find(item => item.value === id);
    return docStatus?.description.toUpperCase();
  }

  statusUpdate(status: number): void {
    this.docStatus_number = status;

    if (status === 1) {
      this.isNA = false;
      this.isSentApprove = true;
      this.isCancel = true;
    } else if (status === 2) {
      this.isNA = false;
      this.isSentApprove = false;
      this.isCancel = false;
    } else if (status === 3) {
      this.isNA = false;
      this.isSentApprove = false;
      this.isCancel = false;
    } else if (status === 4) {
      this.isNA = false;
      this.isSentApprove = true;
      this.isCancel = false;
    } else if (status === 5) {
      this.isNA = false;
      this.isSentApprove = false;
      this.isCancel = false;
    } else {
      this.isNA = false;
      this.isSentApprove = false;
      this.isCancel = false;
    }
  }

  protected subscribeToSaveResponseCheckFileexist(result: Observable<HttpResponse<IReplyMessage>>, i: number): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccessCheckFileexist(res, i),
      () => this.onSaveErrorCheckFileexist()
    );
  }

  protected onSaveSuccessCheckFileexist(result: HttpResponse<IReplyMessage>, i: number): void {
    const replyMessage: IReplyMessage | null = result.body;

    if (replyMessage !== null) {
      if (replyMessage.code === ResponseCode.ERROR_E00) {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        // this.showAlertMessage(replyCode, replyMsg);
        this.removeFieldConfirm(i);
      } else {
        this.removeFieldConfirm(i);
      }
    } else {
      this.onSaveErrorCheckFileexist();
    }
  }

  protected onSaveErrorCheckFileexist(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
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
      if (replyMessage.code === ResponseCode.SUCCESS) {
        this.editForm.get(['id'])?.setValue(replyMessage.data.id);

        this._documentHeader = replyMessage.data;
        this._documentDetails = this._documentHeader?.docList;
        this.removeAllField();
        this.updateForm(replyMessage.data);

        console.log('Document Header xxxxxxxx', this._documentHeader);

        this.statusUpdate(replyMessage.data.status);
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.isDocMap = true;
        this.isUploadDetail = false;
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

  protected subscribeTeUpdateStatusResponse(result: Observable<HttpResponse<IReplyMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccessUpdateStatus(res),
      () => this.onSaveErrorUpdateStatus()
    );
  }

  protected onSaveSuccessUpdateStatus(result: HttpResponse<IReplyMessage>): void {
    const replyMessage: IReplyMessage | null = result.body;

    if (replyMessage !== null) {
      if (replyMessage.code === ResponseCode.SUCCESS) {
        this.statusUpdate(this.docStatus_number);
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

  protected onSaveErrorUpdateStatus(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
  }

  protected createFromForm(paraStatus: number): IDocumentHeader {
    if (this.docStatus_number > 0) {
      paraStatus = this.docStatus_number;
    }

    return {
      ...new DocumentHeader(),
      id: this.editForm.get(['id'])!.value,
      metaDataHeaderId: this.editForm.get(['metaDataHeaderId'])!.value,
      message: this.editForm.get(['message'])!.value,
      reasonForAmend: this.editForm.get(['ammendment'])!.value,
      reasonForReject: this.editForm.get(['reject'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      status: paraStatus,
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
      ...new DMSDocument(),
      id: data.get(['id'])!.value,
      headerId: undefined,
      filePath: data.get(['filePath'])!.value,
      fileName: data.get(['fileName'])!.value,
      fileNameVersion: data.get(['fileNameVersion'])!.value,
      fileSize: data.get(['fileSize'])!.value,
      version: data.get(['version'])!.value,
      remark: data.get(['remark'])!.value,
      delFlag: 'N',
      fileData: data.get(['fileData'])!.value,
    };
  }

  protected updateForm(docHeaderData: IDocumentHeader): void {
    this.loadMetaDatabyMetadaHeaderID(docHeaderData.metaDataHeaderId!);
    this.updateDynamicField(docHeaderData.metaDataHeaderId!, docHeaderData.fieldValues!, docHeaderData.fieldNames!);
    this.statusUpdate(docHeaderData.status!);

    this.editForm.patchValue({
      id: docHeaderData.id,
      metaDataHeaderId: docHeaderData.metaDataHeaderId,
      message: docHeaderData.message,
      ammendment: docHeaderData.reasonForAmend,
      reject: docHeaderData.reasonForReject,
      fieldValues: docHeaderData.fieldValues,
      fieldNames: docHeaderData.fieldNames,
      priority: docHeaderData.priority,
      status: docHeaderData.status,
      docList: this.updateDocumentDataDetails(docHeaderData.docList),
    });
  }

  protected updateDynamicField(metaDataHeaderId: number, fieldValue: string, fieldName: string): void {
    const fn = fieldName.split('|');
    const fv = fieldValue.split('|');

    this._metaDataHdrList!.forEach((metaDataHeaderItem: IMetaDataHeader) => {
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
      this.docList1().controls[index].get(['id'])!.setValue(data.id);
      this.docList1().controls[index].get(['filePath'])!.setValue(data.filePath);
      this.docList1().controls[index].get(['fileName'])!.setValue(data.fileName);
      this.docList1().controls[index].get(['fileNameVersion'])!.setValue(data.fileNameVersion);
      this.docList1().controls[index].get(['fileSize'])!.setValue(data.fileSize);
      this.docList1().controls[index].get(['remark'])!.setValue(data.remark);
      this.docList1().controls[index].get(['version'])!.setValue(data.version);
      index = index + 1;
    });
  }
}
