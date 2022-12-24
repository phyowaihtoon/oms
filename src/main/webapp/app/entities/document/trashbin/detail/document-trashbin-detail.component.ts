import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import * as FileSaver from 'file-saver';
import { DMSDocument, DocumentHeader, IDocument, IDocumentHeader } from '../../document.model';
import { DocumentInquiryService } from '../../service/document-inquiry.service';
import { Event as NavigationEvent } from '@angular/router';
import { IDocumentStatus, IPriority } from 'app/entities/util/setup.model';
import { AbstractControl, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-document-trashbin-detail',
  templateUrl: './document-trashbin-detail.component.html',
  styleUrls: ['./document-trashbin-detail.component.scss'],
})
export class DocumentTrashbinDetailComponent implements OnInit {
  _documentHeader?: IDocumentHeader;
  _replyMessage?: IReplyMessage | null;
  _documentStatus?: IDocumentStatus[];
  _priority?: IPriority[];
  _docExtensionTypes = [
    { extension: 'pdf', value: 'PDF' },
    { extension: 'docx', value: 'WORD' },
    { extension: 'xls', value: 'EXCEL' },
    { extension: 'xlsx', value: 'EXCEL' },
    { extension: 'jpg', value: 'IMAGE' },
    { extension: 'jpeg', value: 'IMAGE' },
    { extension: 'png', value: 'IMAGE' },
    { extension: 'txt', value: 'TEXT' },
    { extension: 'csv', value: 'CSV' },
  ];
  _metaDataHdrList?: IMetaDataHeader[] | null;
  _modalRef?: NgbModalRef;
  _isDocHeaderShow = false;
  _isDocDetailShow = true;
  _checkedItemList: number[] = [];

  editForm = this.fb.group({
    id: [],
    docList: this.fb.array([]),
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService,
    protected documentInquiryService: DocumentInquiryService,
    protected modalService: NgbModal,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        if (event.restoredState) {
          console.log('Navigation ID:', event.id);
          console.log('Navigation URL:', event.url);
          console.log('trigger:', event.navigationTrigger);
          console.warn('restoring navigation id:', event.restoredState.navigationId);
          this.documentInquiryService.setPreviousState('DocumentDetial');
        }
      }
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ docHeader }) => {
      this._documentHeader = docHeader;
      if (this._documentHeader) {
        this.updateForm(this._documentHeader);
      }
    });
    this.loadAllSetup();
  }

  loadAllSetup(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this._metaDataHdrList = res.body;
      },
      error => {
        console.log('MetaDataHeader Failed : ', error);
      }
    );
    this.loadSetupService.loadDocumentStatus().subscribe(
      (res: HttpResponse<IDocumentStatus[]>) => {
        if (res.body) {
          this._documentStatus = res.body;
        }
      },
      error => {
        console.log('Document Status Failed : ', error);
      }
    );
    this.loadSetupService.loadPriority().subscribe(
      (res: HttpResponse<IPriority[]>) => {
        if (res.body) {
          this._priority = res.body;
        }
      },
      error => {
        console.log('Priority Failed : ', error);
      }
    );
  }

  restoreDocument(): void {
    const documentHeader = this.createFromForm();
    this.documentInquiryService.restoreDocument(documentHeader).subscribe(
      (response: HttpResponse<IReplyMessage>) => {
        const replyMessage: IReplyMessage | null = response.body;
        if (replyMessage !== null) {
          if (replyMessage.code === ResponseCode.SUCCESS) {
            const replyCode = replyMessage.code;
            const replyMsg = replyMessage.message;
            this.showAlertMessage(replyCode, replyMsg);

            // Remove items from list after restored
            if (this._checkedItemList.length > 0) {
              this._checkedItemList.forEach(value => {
                this.docList1().removeAt(value);
              });
            }
          } else {
            const replyCode = replyMessage.code;
            const replyMsg = replyMessage.message;
            this.showAlertMessage(replyCode, replyMsg);
          }
        } else {
          const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
          const replyMsg = 'Internal Server Error';
          this.showAlertMessage(replyCode, replyMsg);
        }
      },
      error => {
        const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
        const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
        this.showAlertMessage(replyCode, replyMsg);
      }
    );
  }

  validate(isPreview: boolean, filePath: string): boolean {
    let message1 = '',
      message2 = '';
    if (isPreview) {
      const fileExtension = filePath.split('.').pop();
      if (fileExtension === undefined || fileExtension.trim().length === 0) {
        message1 = 'Invalid File Type.';
      }

      if (fileExtension?.toLowerCase() !== 'pdf') {
        message1 = 'Preview is only available for PDF documents.';
        message2 = 'You can download the file and view it.';
      }

      if (message1.length > 0) {
        this.showAlertMessage(message1, message2);
        return false;
      }
    }

    return true;
  }

  showAlertMessage(msg1: string, msg2: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  previewFile(index: number): void {
    const docId = this.docList1().controls[index].get(['id'])!.value;
    const fileName = this.docList1().controls[index].get(['fileName'])!.value;

    if (docId !== undefined && fileName !== undefined && this.validate(true, fileName)) {
      this.showLoading('Loading File');
      this.documentInquiryService.previewFile(docId).subscribe(
        (res: HttpResponse<Blob>) => {
          if (res.status === 200 && res.body) {
            const modalRef = this.modalService.open(PdfViewerComponent, { size: 'xl', backdrop: 'static', centered: true });
            modalRef.componentInstance.pdfBlobURL = res.body;
          } else if (res.status === 204) {
            const code = ResponseCode.WARNING;
            const message = 'This file does not exist.';
            this.showAlertMessage(code, message);
          } else if (res.status === 205) {
            const code = ResponseCode.ERROR_E00;
            const message = 'Invalid file type.';
            this.showAlertMessage(code, message);
          } else {
            const code = ResponseCode.ERROR_E00;
            const message = 'Cannot download file';
            this.showAlertMessage(code, message);
          }
          this.hideLoading();
        },
        error => {
          this.hideLoading();
          const code = ResponseCode.RESPONSE_FAILED_CODE;
          const message = 'Error occured while connecting to server. Please, check network connection with your server.';
          this.showAlertMessage(code, message);
        }
      );
    }
  }

  downloadFile(index: number): void {
    const docId = this.docList1().controls[index].get(['id'])!.value;
    const fileName = this.docList1().controls[index].get(['fileName'])!.value;

    if (docId !== undefined && fileName !== undefined && this.validate(false, fileName)) {
      this.showLoading('Downloading File');
      this.documentInquiryService.downloadFile(docId).subscribe(
        (res: HttpResponse<Blob>) => {
          if (res.status === 200 && res.body) {
            FileSaver.saveAs(res.body, fileName);
          } else if (res.status === 204) {
            const code = ResponseCode.WARNING;
            const message = 'This file does not exist.';
            this.showAlertMessage(code, message);
          } else if (res.status === 205) {
            const code = ResponseCode.ERROR_E00;
            const message = 'Invalid file type.';
            this.showAlertMessage(code, message);
          } else {
            const code = ResponseCode.ERROR_E00;
            const message = 'Cannot download file';
            this.showAlertMessage(code, message);
          }
          this.hideLoading();
        },
        error => {
          this.hideLoading();
          const code = ResponseCode.RESPONSE_FAILED_CODE;
          const message = 'Error occured while connecting to server. Please, check network connection with your server.';
          this.showAlertMessage(code, message);
        }
      );
    }
  }

  showLoading(loadingMessage?: string): void {
    this._modalRef = this.modalService.open(LoadingPopupComponent, { size: 'sm', backdrop: 'static', centered: true });
    this._modalRef.componentInstance.loadingMessage = loadingMessage;
  }

  hideLoading(): void {
    this._modalRef?.close();
  }

  getDocumentStatus(id?: number): string | undefined {
    const docStatus = this._documentStatus?.find(item => item.value === id);
    return docStatus?.description.toUpperCase();
  }

  getPriorityDesc(id?: number): string | undefined {
    const priority = this._priority?.find(item => item.value === id);
    return priority?.description;
  }

  getDocTitleByID(id?: number): string | undefined {
    const metaDataHeader = this._metaDataHdrList?.find(item => item.id === id);
    return metaDataHeader?.docTitle;
  }

  getFileType(fileName?: string): string | undefined {
    let fileType = '';
    if (fileName !== undefined) {
      const fileExtension = fileName.split('.').pop();
      if (fileExtension === undefined || fileExtension.trim().length === 0) {
        return fileType;
      }

      const docExtensionType = this._docExtensionTypes.find(item => item.extension === fileExtension);
      if (docExtensionType) {
        fileType = docExtensionType.value;
      } else {
        fileType = fileExtension;
      }
    }
    return fileType;
  }

  getMetaDataFieldsAndValues(fNames?: string, fValues?: string): any {
    const keyValues = [];
    if (fNames !== undefined && fValues !== undefined && fNames.trim().length > 0 && fValues.trim().length > 0) {
      const fNameArray = fNames.split('|');
      const fValueArray = fValues.split('|');
      if (fNameArray.length > 0 && fValueArray.length > 0 && fNameArray.length === fValueArray.length) {
        let arrIndex = 0;
        while (arrIndex < fNameArray.length) {
          const keyValue = { name: '', value: '' };
          keyValue.name = fNameArray[arrIndex];
          keyValue.value = fValueArray[arrIndex];
          keyValues.push(keyValue);
          arrIndex++;
        }
      }
    }
    return keyValues;
  }

  previousState(): void {
    window.history.back();
  }

  showDocHeader(): void {
    if (this._isDocHeaderShow === false) {
      this._isDocHeaderShow = !this._isDocHeaderShow;
    }

    this._isDocDetailShow = false;
  }

  showDocDetail(): void {
    if (this._isDocDetailShow === false) {
      this._isDocDetailShow = !this._isDocDetailShow;
    }
    this._isDocHeaderShow = false;
  }

  // define FormArray for document List
  docList1(): FormArray {
    return this.editForm.get('docList') as FormArray;
  }

  // create new field dynamically
  newField(filePath: string, fileName: string, fileSize: number): FormGroup {
    return this.fb.group({
      id: [],
      filePath: [filePath, [Validators.required]],
      fileName: [fileName, [Validators.required]],
      fileNameVersion: [],
      fileSize: [fileSize, [Validators.required]],
      version: [''],
      remark: [''],
      restoreChecked: [false],
    });
  }

  // add new field dynamically
  addField(filePath: string, fileName: string, fileSize: number): void {
    this.docList1().push(this.newField(filePath, fileName, fileSize));
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
      this.docList1().controls[index].get(['version'])!.setValue(data.version);
      this.docList1().controls[index].get(['remark'])!.setValue(data.remark);
      this.docList1().controls[index].get(['restoreChecked'])!.setValue(false);
      index = index + 1;
    });
  }

  protected updateForm(docHeaderData: IDocumentHeader): void {
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

  protected createFromForm(): IDocumentHeader {
    return {
      ...new DocumentHeader(),
      id: this.editForm.get(['id'])!.value,
      docList: this.createFileListDetails(),
    };
  }

  protected createFileListDetails(): IDocument[] {
    this._checkedItemList = [];
    const fieldList: IDocument[] = [];
    this.docList1().controls.forEach((frmControl, index) => {
      if (frmControl.get(['restoreChecked'])!.value) {
        fieldList.push(this.createFileListDetail(frmControl));
        this._checkedItemList.push(index);
      }
    });
    return fieldList;
  }

  protected createFileListDetail(frmControl: AbstractControl): IDocument {
    return {
      ...new DMSDocument(),
      id: frmControl.get(['id'])!.value,
      filePath: frmControl.get(['filePath'])!.value,
      fileName: frmControl.get(['fileName'])!.value,
      fileSize: frmControl.get(['fileSize'])!.value,
      version: frmControl.get(['version'])!.value,
      remark: frmControl.get(['remark'])!.value,
    };
  }
}
