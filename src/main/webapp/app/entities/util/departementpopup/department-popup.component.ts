import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'app/login/login.service';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { ResponseCode } from '../reply-message.model';
import { FormBuilder } from '@angular/forms';
import { IDepartment, IHeadDepartment } from 'app/entities/department/department.model';
import { HttpResponse } from '@angular/common/http';
import { LoadSetupService } from '../load-setup.service';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-department-popup',
  templateUrl: './department-popup.component.html',
  styleUrls: ['./department-popup.component.scss'],
})
export class DepartmentPopupComponent implements OnInit, OnDestroy {
  code?: string;
  message?: string;
  logoutFlag?: boolean = false;

  displayCode?: string;
  displayMessage?: string;

  infoTitle?: string;
  modalHeaderClass: string[] = [];
  modalButtonClass: string[] = [];

  headDepartmentList: IHeadDepartment[] = [];
  departmentsList?: IDepartment[];
  departments?: IDepartment[] = [];

  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  isCheckedAll = false;

  constructor(
    public activeModal: NgbActiveModal,
    private userAuthorityService: UserAuthorityService,
    private loginService: LoginService,
    private router: Router,
    protected fb: FormBuilder,
    protected loadSetupService: LoadSetupService,
    protected departmentService: DepartmentService
  ) {}

  ngOnInit(): void {
    this.setHeaderClass();
    this.setButtonClass();

    this.loadSetupService.loadAllHeadDepartments().subscribe(
      (res: HttpResponse<IHeadDepartment[]>) => {
        if (res.body) {
          this.headDepartmentList = res.body;
        }
      },
      error => {
        console.log('Loading Head Department Failed : ', error);
      }
    );

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];
      },
      () => {
        // this.isLoading = false;
        // this.onError();
      }
    );
  }

  trackDepartmentById(index: number, item: IHeadDepartment): number {
    return item.id!;
  }

  onChangeSelectedAll(e: any): void {
    // debugger;
    if (this.departments) {
      if (e.target.checked) {
        for (let i = 0; i < this.departments.length; i++) {
          this.departments[i].isChecked = true;
        }
      } else {
        for (let i = 0; i < this.departments.length; i++) {
          this.departments[i].isChecked = false;
        }
      }
    }
  }

  onChangeSelected(e: any, i: number): void {
    if (this.departments) {
      if (e.target.checked) {
        // this.selectedCount = this.selectedCount + 1;
        this.departments[i].isChecked = true;
        let chkAll = true;
        for (let j = 0; j < this.departments.length; j++) {
          if (this.departments[j].isChecked === false || this.departments[j].isChecked === undefined) {
            chkAll = false;
            break;
          }
        }
        this.isCheckedAll = chkAll;
      } else {
        // this.selectedCount = this.selectedCount - 1;
        this.departments[i].isChecked = false;
        this.isCheckedAll = false;
      }
    }
  }

  onDepartmentChange(e: any): void {
    this.departments = [];
    this.departmentsList?.forEach(data => {
      if (data.headDepartment?.id === Number(e.target.value)) {
        this.departments?.push(data);
      }
    });
  }

  dismiss(): void {
    this.activeModal.dismiss();
    if (this.logoutFlag && this.code === ResponseCode.SUCCESS) {
      this.logout();
    }
  }

  add(): void {
    const output: IDepartment[] = [];
    if (this.departments) {
      this.departments.forEach(data => {
        if (data.isChecked === true) {
          output.push(data);
        }
      });
    }

    this.passEntry.emit(output);
    this.activeModal.dismiss();
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
    const infoHeader = ['modal-header', 'dms-modal-header', ' bg-dark', 'text-white'];

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
