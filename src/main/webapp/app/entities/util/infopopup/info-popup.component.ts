import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ResponseCode } from '../reply-message.model';

@Component({
  selector: 'jhi-info-popup',
  templateUrl: './info-popup.component.html',
  styleUrls: ['./info-popup.component.scss'],
})
export class InfoPopupComponent implements OnInit, OnDestroy {
  code?: string;
  message?: string;

  displayCode?: string;
  displayMessage?: string;

  infoTitle?: string;
  modalHeaderClass: string[] = [];
  modalButtonClass: string[] = [];

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    this.setHeaderClass();
    this.setButtonClass();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  setHeaderClass(): void {
    const successHeader = ['modal-header', 'dms-modal-header', 'bg-success', 'text-white'];
    const errorHeader = ['modal-header', 'dms-modal-header', 'bg-danger', 'text-white'];
    const warningHeader = ['modal-header', 'dms-modal-header', 'bg-warning', 'text-white'];
    const infoHeader = ['modal-header', 'dms-modal-header', 'bg-info', 'text-white'];

    if (this.code === ResponseCode.SUCCESS) {
      this.modalHeaderClass = successHeader;
      this.infoTitle = ResponseCode.SUCCESS_MSG;
      this.displayCode = 'Code : '.concat(this.code);
      this.displayMessage = 'Message : '.concat(this.message ? this.message : '');
    } else if (this.code === ResponseCode.ERROR_E00 || this.code === ResponseCode.ERROR_E01) {
      this.modalHeaderClass = errorHeader;
      this.infoTitle = ResponseCode.ERROR_MSG;
      this.displayCode = 'Code : '.concat(this.code);
      this.displayMessage = 'Message : '.concat(this.message ? this.message : '');
    } else if (this.code === ResponseCode.WARNING) {
      this.modalHeaderClass = warningHeader;
      this.infoTitle = ResponseCode.WARNING_MSG;
      this.displayCode = 'Code : '.concat(this.code);
      this.displayMessage = 'Message : '.concat(this.message ? this.message : '');
    } else if (this.code === ResponseCode.RESPONSE_FAILED_CODE) {
      this.modalHeaderClass = errorHeader;
      this.infoTitle = ResponseCode.RESPONSE_FAILED_MSG;
      this.displayCode = 'Code : '.concat(this.code);
      this.displayMessage = 'Message : '.concat(this.message ? this.message : '');
    } else {
      this.modalHeaderClass = infoHeader;
      this.infoTitle = 'Information';
      this.displayCode = this.code;
      this.displayMessage = this.message;
    }
  }

  setButtonClass(): void {
    const btnSuccess = ['btn', 'btn-success'];
    const btnDanger = ['btn', 'btn-danger'];
    const btnWarning = ['btn', 'btn-warning'];
    const btnInfo = ['btn', 'btn-info'];

    if (this.code === ResponseCode.SUCCESS) {
      this.modalButtonClass = btnSuccess;
    } else if (this.code === ResponseCode.ERROR_E00 || this.code === ResponseCode.ERROR_E01) {
      this.modalButtonClass = btnDanger;
    } else if (this.code === ResponseCode.WARNING) {
      this.modalButtonClass = btnWarning;
    } else if (this.code === ResponseCode.RESPONSE_FAILED_CODE) {
      this.modalButtonClass = btnDanger;
    } else {
      this.modalButtonClass = btnInfo;
    }
  }

  ngOnDestroy(): void {
    this.code = '';
    this.message = '';
    this.displayCode = '';
    this.displayMessage = '';
    this.infoTitle = '';
    this.modalHeaderClass = [];
    this.modalButtonClass = [];
  }
}
