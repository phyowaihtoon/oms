import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'app/login/login.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { ResponseCode } from '../reply-message.model';

@Component({
  selector: 'jhi-info-popup',
  templateUrl: './info-popup.component.html',
  styleUrls: ['./info-popup.component.scss'],
})
export class InfoPopupComponent implements OnInit, OnDestroy {
  code?: string;
  message?: string;
  logoutFlag?: boolean = false;

  displayCode?: string;
  displayMessage?: string;

  infoTitle?: string;
  modalHeaderClass: string[] = [];
  modalButtonClass: string[] = [];

  constructor(
    public activeModal: NgbActiveModal,
    private userAuthorityService: UserAuthorityService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.setHeaderClass();
    this.setButtonClass();
  }

  dismiss(): void {
    this.activeModal.dismiss();
    if (this.logoutFlag && this.code === ResponseCode.SUCCESS) {
      this.logout();
    }
  }

  logout(): void {
    this.userAuthorityService.clearUserAuthority();
    this.loginService.logout();
    this.router.navigate(['/login']);
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
    } else if (this.code === ResponseCode.ERROR_E00 || this.code === ResponseCode.EXCEP_EX) {
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
    } else if (this.code === ResponseCode.ERROR_E00 || this.code === ResponseCode.EXCEP_EX) {
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
