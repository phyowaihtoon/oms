import { Component, OnInit } from '@angular/core';
import { IDocumentAttachment, IDocumentDelivery, IDocumentReceiver } from '../delivery.model';
import { ActivatedRoute } from '@angular/router';
import { IDepartment } from 'app/entities/department/department.model';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { HttpResponse } from '@angular/common/http';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import * as FileSaver from 'file-saver';
import { DeliveryService } from '../service/delivery.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-delivery-detail',
  templateUrl: './delivery-detail.component.html',
  styleUrls: ['./delivery-detail.component.scss'],
})
export class DeliveryDetailComponent implements OnInit {
  isLoading = false;
  documentDelivery?: IDocumentDelivery;
  receiverList?: IDocumentReceiver[] = [];
  attachmentList?: IDocumentAttachment[] = [];
  docNo: string | undefined;
  subject: string | undefined;
  bodyDescription: string | undefined;
  senderDepartment: string | undefined;
  _modalRef?: NgbModalRef;
  _departmentName: string | undefined = '';
  isInfo = true;
  isUploadDetail = false;

  toDepartments?: IDepartment[] = [];
  ccDepartments?: IDepartment[] = [];
  modules = {};
  _loginDepartment?: IDepartment;
  _docStatus = { status: 0, description: '', actionLabel: 'Mark As Read' };

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    protected deliveryService: DeliveryService,
    protected userAuthorityService: UserAuthorityService,
    protected translateService: TranslateService
  ) {
    this.modules = {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'], // toggled buttons
        ['blockquote', 'code-block'],

        [{ color: [] }, { background: [] }], // dropdown with defaults from theme
        [{ font: [] }],
        [{ align: [] }],
      ],
    };
  }

  ngOnInit(): void {
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;
    this._loginDepartment = userAuthority?.department;

    this.activatedRoute.data.subscribe(({ delivery }) => {
      this.documentDelivery = delivery?.documentDelivery;
      this.receiverList = delivery?.receiverList;
      this.attachmentList = delivery?.attachmentList;
    });

    this.getData();
  }

  previousState(): void {
    window.history.back();
  }

  getData(): void {
    this.docNo = this.documentDelivery?.referenceNo;
    this.subject = this.documentDelivery?.subject;
    this.bodyDescription = this.documentDelivery?.description;
    this.senderDepartment = this.documentDelivery?.sender?.departmentName;

    if (this._loginDepartment?.id === this.documentDelivery?.sender?.id) {
      if (this.documentDelivery?.status === 1) {
        this._docStatus.status = 1;
        this._docStatus.description = 'Read';
        this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsUnRead');
      } else {
        this._docStatus.status = 0;
        this._docStatus.description = 'Unread';
        this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsRead');
      }
    }

    this.receiverList?.forEach((value: IDocumentReceiver, index) => {
      if (value.receiverType === 1) {
        this.toDepartments?.push(value.receiver!);
      } else {
        this.ccDepartments?.push(value.receiver!);
      }

      if (this._loginDepartment?.id === value.receiver?.id) {
        if (value.status === 1) {
          this._docStatus.status = 1;
          this._docStatus.description = 'Read';
          this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsUnRead');
        } else {
          this._docStatus.status = 0;
          this._docStatus.description = 'Unread';
          this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsRead');
        }
      }
    });
  }

  markAsReadUnread(): void {
    if (this.documentDelivery?.id) {
      // Changing Status from Unread to Read
      if (this._docStatus.status === 0) {
        this.isLoading = true;
        this.showLoading('Updating Document Status');
        this.deliveryService.markAsRead(this.documentDelivery.id).subscribe(
          (res: HttpResponse<IReplyMessage>) => {
            const replyMessage: IReplyMessage | null = res.body;
            this.isLoading = false;
            this.hideLoading();

            if (replyMessage !== null) {
              if (replyMessage.code === ResponseCode.SUCCESS) {
                this._docStatus.status = 1;
                this._docStatus.description = 'Read';
                this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsUnRead');
                const replyCode = replyMessage.code;
                const replyMsg = replyMessage.message;
                this.showAlertMessage(replyCode, replyMsg);
              } else {
                const replyCode = replyMessage.code;
                const replyMsg = replyMessage.message;
                this.showAlertMessage(replyCode, replyMsg);
              }
            }
          },
          res => {
            this.isLoading = false;
            this.hideLoading();
            const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
            const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
            this.showAlertMessage(replyCode, replyMsg);
          }
        );
      }

      // Changing Status from Read to Unread
      if (this._docStatus.status === 1) {
        this.isLoading = true;
        this.showLoading('Updating Document Status');
        this.deliveryService.markAsUnRead(this.documentDelivery.id).subscribe(
          (res: HttpResponse<IReplyMessage>) => {
            const replyMessage: IReplyMessage | null = res.body;
            this.isLoading = false;
            this.hideLoading();

            if (replyMessage !== null) {
              if (replyMessage.code === ResponseCode.SUCCESS) {
                this._docStatus.status = 0;
                this._docStatus.description = 'Unread';
                this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsRead');
                const replyCode = replyMessage.code;
                const replyMsg = replyMessage.message;
                this.showAlertMessage(replyCode, replyMsg);
              } else {
                const replyCode = replyMessage.code;
                const replyMsg = replyMessage.message;
                this.showAlertMessage(replyCode, replyMsg);
              }
            }
          },
          res => {
            this.isLoading = false;
            this.hideLoading();
            const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
            const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
            this.showAlertMessage(replyCode, replyMsg);
          }
        );
      }
    }
  }

  showLoading(loadingMessage?: string): void {
    this._modalRef = this.modalService.open(LoadingPopupComponent, { size: 'sm', backdrop: 'static', centered: true });
    this._modalRef.componentInstance.loadingMessage = loadingMessage;
  }

  hideLoading(): void {
    this._modalRef?.close();
  }

  showAlertMessage(msg1: string, msg2: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
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

  previewFile(docId?: number, fileName?: string): void {
    if (docId !== undefined && fileName !== undefined && this.validate(true, fileName)) {
      this.showLoading('Loading File');
      this.deliveryService.getPreviewData(docId).subscribe(
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
        () => {
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
      this.deliveryService.downloadFile(docId).subscribe(
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
        () => {
          this.hideLoading();
          const code = ResponseCode.RESPONSE_FAILED_CODE;
          const message = 'Error occured while connecting to server. Please, check network connection with your server.';
          this.showAlertMessage(code, message);
        }
      );
    }
  }

  showInfo(): void {
    if (!this.isInfo) {
      this.isInfo = !this.isInfo;
    }
    this.isUploadDetail = false;
  }

  showUploadDetails(): void {
    if (!this.isUploadDetail) {
      this.isUploadDetail = !this.isUploadDetail;
    }

    this.isInfo = false;
  }
}
