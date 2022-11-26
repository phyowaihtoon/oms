import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, NavigationStart, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { PdfViewerComponent } from 'app/entities/util/pdfviewer/pdf-viewer.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import * as FileSaver from 'file-saver';
import { IDocument, IDocumentHeader } from '../document.model';
import { DocumentInquiryService } from '../service/document-inquiry.service';
import { filter } from 'rxjs/operators';
import { Event as NavigationEvent } from '@angular/router';
import { IDocumentStatus, IPriority } from 'app/entities/util/setup.model';

@Component({
  selector: 'jhi-document-detail',
  templateUrl: './document-detail.component.html',
  styleUrls: ['./document-detail.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DocumentDetailComponent implements OnInit {
  _documentHeader: IDocumentHeader | null = null;
  _documentDetails: IDocument[] | undefined;
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

  _isDocHeaderShow = true;
  _isDocDetailShow = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService,
    protected documentInquiryService: DocumentInquiryService,
    protected modalService: NgbModal,
    protected router: Router
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

  downloadFile(isPreview: boolean, docId?: number, fileName?: string): void {
    if (docId !== undefined && fileName !== undefined && this.validate(isPreview, fileName)) {
      this.showLoading(isPreview === true ? 'Loading File' : 'Downloading File');
      this.documentInquiryService.downloadFile(docId).subscribe(
        (res: HttpResponse<Blob>) => {
          if (res.status === 200 && res.body) {
            if (isPreview) {
              const modalRef = this.modalService.open(PdfViewerComponent, { size: 'xl', backdrop: 'static', centered: true });
              modalRef.componentInstance.pdfBlobURL = res.body;
            } else {
              FileSaver.saveAs(res.body, fileName);
            }
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
    this.documentInquiryService.setPreviousState('DocumentDetial');
    this.router.navigate(['/document/list']);
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
}
