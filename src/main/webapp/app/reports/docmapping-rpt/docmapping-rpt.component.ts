import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { IRptParamsDTO, RptParamsDTO } from '../report.model';
import { ReportService } from '../service/report.service';

@Component({
  selector: 'jhi-docmapping-rpt',
  templateUrl: './docmapping-rpt.component.html',
  styleUrls: ['./docmapping-rpt.component.scss'],
})
export class DocMappingRptComponent {
  isGenerating = false;
  _replyMessage: IReplyMessage | null = null;
  _messageCode?: string;
  _alertMessage?: string;
  isShowingAlert = false;
  _modalRef?: NgbModalRef;
  rptParams?: IRptParamsDTO;
  rptTitleName: string = 'Document Mapping Report';
  rptFileName: string = 'DocumentMappingRpt';
  rptFormat: string = '.pdf';

  editForm = this.fb.group({
    rptTitleName: [],
    rptFileName: [],
    rptFormat: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
  });

  constructor(
    private reportService: ReportService,
    protected fb: FormBuilder,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  generate(): void {
    this.isGenerating = true;
    const reportData = this.createFromForm();
    this.showLoading('Generating Report');
    this.reportService.generateDocMappingListRpt(reportData).subscribe(
      res => {
        setTimeout(() => this._modalRef?.close(), 1000);

        this._replyMessage = res.body;

        if (this._replyMessage?.code === '000') {
          this.rptParams = this._replyMessage.data;
          this.router.navigate(['report/report-viewer'], {
            queryParams: {
              rptTitleName: this.rptParams?.rptTitleName,
              rptFileName: this.rptParams?.rptFileName,
              rptFormat: this.rptParams?.rptFormat,
            },
          });
        } else {
          this.isShowingAlert = true;
          this.isGenerating = false;
          this._messageCode = this._replyMessage?.code;
          this._alertMessage = this._replyMessage?.message;
        }
      },
      error => {
        this._modalRef?.close();
        this.isShowingAlert = true;
        this.isGenerating = false;
        this._messageCode = '';
        this._alertMessage = 'Connection is not available. Please, check network connection with your server.';
      }
    );
  }

  showLoading(loadingMessage?: string): void {
    this._modalRef = this.modalService.open(LoadingPopupComponent, { size: 'sm', backdrop: 'static', centered: true });
    this._modalRef.componentInstance.loadingMessage = loadingMessage;
  }

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  protected createFromForm(): IRptParamsDTO {
    const startDate = this.editForm.get(['startDate'])!.value.format('DD-MM-YYYY');
    const endDate = this.editForm.get(['endDate'])!.value.format('DD-MM-YYYY');
    return {
      ...new RptParamsDTO(),
      rptTitleName: this.rptTitleName,
      rptFileName: this.rptFileName,
      rptFormat: this.rptFormat,
      rptPath: '',
      rptJrxml: '',
      rptPS1: startDate,
      rptPS2: endDate,
      rptPS3: '',
      rptPS4: '',
      rptPS5: '',
    };
  }
}
