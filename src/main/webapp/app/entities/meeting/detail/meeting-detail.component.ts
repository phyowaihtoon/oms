import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IMeetingAttachment, IMeetingDelivery, IMeetingReceiver } from '../meeting.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IDepartment } from 'app/entities/department/department.model';
import { MeetingService } from '../service/meeting.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { TranslateService } from '@ngx-translate/core';
import { HttpResponse } from '@angular/common/http';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import * as FileSaver from 'file-saver';
import * as dayjs from 'dayjs';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-meeting-detail',
  templateUrl: './meeting-detail.component.html',
  styleUrls: ['./meeting-detail.component.scss'],
})
export class MeetingDetailComponent implements OnInit {

  isLoading = false;
  meetingDelivery?: IMeetingDelivery;
  receiverList?: IMeetingReceiver[] = [];
  attachmentList?: IMeetingAttachment[] = [];

  // meetingDate: dayjs.Dayjs | undefined;
  location: string | undefined;
  docNo: string | undefined;
  subject: string | undefined;
  bodyDescription: string | undefined;
  senderDepartment: string | undefined;

  meetingStartDateTime: any;
  meetingEndDateTime: any;
  formattedStartDate:any;
  formattedEndDate:any;
  meetingDate: any;
  meetingStartTime: any;
  meetingEndTime: any;

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
    protected meetingService: MeetingService,
    protected userAuthorityService: UserAuthorityService,
    public datepipe: DatePipe,
    protected translateService: TranslateService) {
      this.modules = {
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'], // toggled buttons
          ['blockquote', 'code-block'],
  
          [{ color: [] }, { background: [] }], // dropdown with defaults from theme
          [{ font: [] }],
          [{ align: [] }],
        ],
      };

    // this.last_date =this.datepipe.transform(new Date(), 'MM/dd/yyyy');
    // console.log("startDate...", this.last_date);
    }

  ngOnInit(): void {
    
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;
    this._loginDepartment = userAuthority?.department;

    this.activatedRoute.data.subscribe(({ meeting }) => {

      console.log("MEETING DELIVERY", meeting);
      

      this.meetingDelivery = meeting?.meetingDelivery;
      this.receiverList = meeting?.receiverList;
      this.attachmentList = meeting?.attachmentList;

     
    }); // 

    this.getData();

  }

  previousState(): void {
    window.history.back();
  }

  
  getData(): void {

    this.formattedStartDate = this.meetingDelivery?.startDate;
    this.meetingStartDateTime = this.datepipe.transform(this.formattedStartDate,'yyyy-MM-dd hh:mm:ss'); 
    const parts = this.meetingStartDateTime.split(" ");
    this.meetingDate = parts[0];
    this.meetingStartTime = parts[1];
    
    console.log("formattedStartDate", this.meetingStartDateTime );
    console.log("this.meetingStartTime", this.meetingStartTime);

    this.formattedEndDate = this.meetingDelivery?.endDate;
    this.meetingEndDateTime = this.datepipe.transform(this.formattedEndDate,'yyyy-MM-dd hh:mm:ss'); 
    const parts2 = this.meetingEndDateTime.split(" ");
    this.meetingEndTime = parts2[1];
    console.log("formattedEndDate", this.meetingEndDateTime );
    console.log("this.meetingEndTime", this.meetingEndTime);

    this.location = this.meetingDelivery?.place; 
    this.docNo = this.meetingDelivery?.referenceNo;
    this.subject = this.meetingDelivery?.subject;
    this.bodyDescription = this.meetingDelivery?.description;
    this.senderDepartment = this.meetingDelivery?.sender?.departmentName;

    if (this._loginDepartment?.id === this.meetingDelivery?.sender?.id) {
      if (this.meetingDelivery?.status === 1) {
        this._docStatus.status = 1;
        this._docStatus.description = 'Read';
        this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsUnRead');
      } else {
        this._docStatus.status = 0;
        this._docStatus.description = 'Unread';
        this._docStatus.actionLabel = this.translateService.instant('omsApp.delivery.label.MarkAsRead');
      }
    }

    this.receiverList?.forEach((value: IMeetingReceiver, index) => {
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
    if (this.meetingDelivery?.id) {
      // Changing Status from Unread to Read
      if (this._docStatus.status === 0) {
        this.isLoading = true;
        this.showLoading('Updating Document Status');
        this.meetingService.markAsRead(this.meetingDelivery.id).subscribe(
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
        this.meetingService.markAsUnRead(this.meetingDelivery.id).subscribe(
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
      this.meetingService.getPreviewData(docId).subscribe(
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
      this.meetingService.downloadFile(docId).subscribe(
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
