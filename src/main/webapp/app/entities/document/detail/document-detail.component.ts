import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import * as FileSaver from 'file-saver';
import { IDocument, IDocumentHeader } from '../document.model';
import { DocumentInquiryService } from '../service/document-inquiry.service';

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

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService,
    protected documentInquiryService: DocumentInquiryService,
    protected modalService: NgbModal
  ) {}

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

      if (fileExtension !== 'pdf') {
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
    modalRef.componentInstance.messageLine1 = msg1;
    modalRef.componentInstance.messageLine2 = msg2;
  }

  downloadFile(isPreview: boolean, docId?: number, filePath?: string): void {
    if (docId !== undefined && filePath !== undefined && this.validate(isPreview, filePath)) {
      const fileName = filePath.split('/').pop();
      this.documentInquiryService.downloadFile(docId).subscribe(
        (res: HttpResponse<Blob>) => {
          if (res.status === 200 && res.body) {
            if (isPreview) {
              const myURL = URL.createObjectURL(res.body);
              window.open(myURL);
            } else {
              FileSaver.saveAs(res.body, fileName);
            }
          } else if (res.status === 204) {
            const msg1 = 'This file does not exist in server.';
            const msg2 = '';
            this.showAlertMessage(msg1, msg2);
          } else {
            const msg1 = 'Connection failed to FTP Server. Please, check network connection to FTP Server.';
            const msg2 = '';
            this.showAlertMessage(msg1, msg2);
          }
        },
        error => {
          const msg1 = 'Connection is not available. Please, check network connection to application server.';
          const msg2 = '';
          this.showAlertMessage(msg1, msg2);
        }
      );
    }
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
}
