import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DocumentInquiry, IDocument, IDocumentHeader, IDocumentInquiry } from 'app/entities/document/document.model';
import { DocumentInquiryService } from 'app/entities/document/service/document-inquiry.service';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { Login } from 'app/login/login.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import * as FileSaver from 'file-saver';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ApproveRejectRemarkComponent } from '../approve-reject-remark/approve-reject-remark.component';

@Component({
  selector: 'jhi-document-queue-update',
  templateUrl: './document-queue-update.component.html',
  styleUrls: ['./document-queue-update.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DocumentQueueUpdateComponent implements OnInit {
  _documentHeader: IDocumentHeader | null = null;
  _documentDetails: IDocument[] | undefined;
  _replyMessage?: IReplyMessage | null;
  _documentHeaderId: number = 0;
  _iDocumentInquiry: IDocumentInquiry | null = null;
  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;

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

  _isDocHeaderShow = true;
  _isDocDetailShow = false;

  _isSentAmmend = true;
  _isApprove = true;
  _isReject = true;

  _docStatus_no: number = 0;
  _docStatus: string = '';

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService,
    protected documentInquiryService: DocumentInquiryService,
    protected documentSevice: DocumentService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ docHeader, userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;

      console.log('user', this._userAuthority);
      console.log('menu', this._activeMenuItem);

      this._documentHeader = docHeader;
      this._documentHeaderId = docHeader.id;
      this._docStatus_no = docHeader.status;
      this.buttonProperties(this._docStatus_no);
      this._documentDetails = this._documentHeader?.docList;
    });
    this.loadAllSetup();
  }

  loadAllSetup(): void {
    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this._metaDataHdrList = res.body;
      },
      error => {
        console.log('Response Failed : ', error);
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

  proceedApproval(status: number): void {
    this._docStatus_no = status;
    if (status !== 3 && status !== 5) {
      const modalRef = this.modalService.open(ApproveRejectRemarkComponent, { size: 'md', backdrop: 'static' });
      if (status === 4) {
        modalRef.componentInstance.title = 'Reason for Ammendment';
      } else {
        modalRef.componentInstance.title = 'Reason for Reject';
      }
      modalRef.componentInstance.passEntry.subscribe((data: any) => {
        const reason = data;
        this.save(status, reason);
      });
    } else {
      this.save(status, '');
    }
  }

  save(status: number, reason: string): void {
    const status_ = status;
    const reason_ = reason;

    const approvalInfo = {
      ...new DocumentInquiry(),
      status: status_,
      reason: reason_,
      approvedBy: this._userAuthority!.userID,
    };

    console.log('approvalinfo', this._userAuthority!.userID);

    this.showLoading('Approving in progress');
    this.subscribeToSaveResponse(this.documentSevice.updateDocumentStatus(approvalInfo, this._documentHeaderId));
  }

  showAlertMessage(msg1: string, msg2: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  previewFile(docId?: number, fileName?: string): void {
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

  downloadFile(docId?: number, fileName?: string): void {
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

  arrangeMetaData(fNames?: string, fValues?: string): string {
    let arrangedFields = '';
    if (fNames !== undefined && fValues !== undefined && fNames.trim().length > 0 && fValues.trim().length > 0) {
      const fNameArray = fNames.split('|');
      const fValueArray = fValues.split('|');
      if (fNameArray.length > 0 && fValueArray.length > 0 && fNameArray.length === fValueArray.length) {
        let arrIndex = 0;
        while (arrIndex < fNameArray.length) {
          const rowStart = "<div class='row col-12'>";
          const col_1_Start = "<div class='col-2 dms-label'>";
          const col_1_Data = '<span>' + fNameArray[arrIndex] + '</span>';
          const col_1_End = '</div>';
          const col_2_Start = "<div class='col-4 dms-view-data'>";
          const col_2_Data = '<span>' + fValueArray[arrIndex] + '</span>';
          const col_2_End = '</div>';
          const rowEnd = '</div>';
          arrangedFields += rowStart + col_1_Start + col_1_Data + col_1_End + col_2_Start + col_2_Data + col_2_End + rowEnd;
          arrIndex++;
        }
      }
    }
    return arrangedFields;
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

  buttonProperties(status: number): void {
    console.log('butt', status);

    if (status !== 2) {
      this._isApprove = false;
      this._isReject = false;
      this._isSentAmmend = false;
    }
    if (this._docStatus_no === 4) {
      this._docStatus = 'SENT TO AMEND';
    } else if (this._docStatus_no === 5) {
      this._docStatus = 'APPROVED';
    } else if (this._docStatus_no === 6) {
      this._docStatus = 'REJECT';
    }
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
        this.buttonProperties(this._docStatus_no);
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
    // this.isSaving = false;
    this.hideLoading();
  }
}
