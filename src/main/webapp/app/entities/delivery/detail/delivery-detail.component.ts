import { Component, OnInit } from '@angular/core';
import { IDocumentAttachment, IDocumentDelivery, IDocumentReceiver } from '../delivery.model';
import { ActivatedRoute } from '@angular/router';
import { IDepartment } from 'app/entities/department/department.model';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { HttpResponse } from '@angular/common/http';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import { ResponseCode } from 'app/entities/util/reply-message.model';
import { error } from 'console';
import * as FileSaver from 'file-saver';
import { DeliveryService } from '../service/delivery.service';

@Component({
  selector: 'jhi-delivery-detail',
  templateUrl: './delivery-detail.component.html',
  styleUrls: ['./delivery-detail.component.scss'],
})
export class DeliveryDetailComponent implements OnInit {

  documentDelivery?: IDocumentDelivery;
  receiverList?: IDocumentReceiver[] = [];
  attachmentList?: IDocumentAttachment[] = [];
  docNo: string | undefined;
  subject: string | undefined;
  body: string | undefined;
  _modalRef?: NgbModalRef;

  toDepartments?: IDepartment[] = [];
  ccDepartments?: IDepartment[] = [];
  modules = {};

  constructor(
    protected activatedRoute: ActivatedRoute,   
    protected modalService: NgbModal,
    protected deliveryService: DeliveryService)
     {
    this.modules = {
      'toolbar': [
        ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
        ['blockquote', 'code-block'],

         [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
         [{ 'font': [] }],
         [{ 'align': [] }],
      ]
    }  
  }

  ngOnInit(): void {
    
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
    const tolist = this.receiverList!.map(receiverDept => receiverDept.receiver);
    this.docNo = this.documentDelivery?.referenceNo;
    this.subject = this.documentDelivery?.subject;
    this.body = this.documentDelivery?.description;

      
    this.receiverList?.forEach((value, index) => {
      if(value.receiverType === 1){
        this.toDepartments?.push(value.receiver!) ;
      }else{
        this.ccDepartments?.push(value.receiver!) ;
      }
    });

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
}

