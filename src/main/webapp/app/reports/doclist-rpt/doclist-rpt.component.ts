import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { IReplyMessage } from 'app/entities/util/reply-message.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { IRptParamsDTO, RptParamsDTO } from '../report.model';
import { ReportService } from '../service/report.service';
import { HttpResponse } from '@angular/common/http';
import { IDepartment } from 'app/entities/department/department.model';
import { UserAuthorityService } from 'app/login/userauthority.service';

@Component({
  selector: 'jhi-doclist-rpt',
  templateUrl: './doclist-rpt.component.html',
  styleUrls: ['./doclist-rpt.component.scss'],
})
export class DoclistRptComponent implements OnInit {
  isGenerating = false;
  _replyMessage: IReplyMessage | null = null;
  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;
  _messageCode?: string;
  _alertMessage?: string;
  isShowingAlert = false;
  _modalRef?: NgbModalRef;
  rptParams?: IRptParamsDTO;
  rptTitleName: string = 'Document Received Report';
  rptFileName: string = 'DocumentReceivedListRpt';
  rptFormat: string = '.pdf';
  departmentsList?: IDepartment[];
  _logindepartmentName: string | undefined = '';
  _loginDepartmentId: number | undefined = 0;
  _lStatus = 2;

  editForm = this.fb.group({
    rptTitleName: [],
    rptFileName: [],
    rptFormat: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    departmentID: [],
    userId: [''],
  });

  constructor(
    private reportService: ReportService,
    protected loadSetupService: LoadSetupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected router: Router,
    protected modalService: NgbModal,
    protected userAuthorityService: UserAuthorityService
  ) {}

  ngOnInit(): void {
    const _userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._logindepartmentName = _userAuthority?.department?.departmentName;
    this._loginDepartmentId = _userAuthority?.department?.id;

    this.loadSetupService.loadAllSubDepartments().subscribe(
      (res: HttpResponse<IDepartment[]>) => {
        this.departmentsList = res.body ?? [];
      },
      error => {
        console.log(error);
      }
    );
  }

  trackDepartmentByID(index: number, item: IDepartment): number {
    return item.id!;
  }

  generate(): void {
    this.isGenerating = true;
    const reportData = this.createFromForm();
    this.showLoading('Generating Report');
    this.reportService.generateDocReceivedListRpt(reportData).subscribe(
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

  clearFormData(): void {
    this.editForm.reset();
  }

  closeAlert(): void {
    this.isShowingAlert = false;
  }

  onStatusChange(e: any): void {
    this._lStatus = e.target.value;
  }

  getDepartmentNameById(id: number): string | undefined {
    const dept = this.departmentsList!.find(obj => obj.id === id);
    return dept ? dept.departmentName : undefined;
  }

  protected createFromForm(): IRptParamsDTO {
    const startDate = this.editForm.get(['startDate'])!.value.format('DD-MM-YYYY');
    const endDate = this.editForm.get(['endDate'])!.value.format('DD-MM-YYYY');
    const departmentID = this.editForm.get(['departmentID'])!.value;

    // const logindepartmentID = this.editForm.get(['departmentID'])!.value;
    // const userID = this.editForm.get(['userId'])!.value;

    return {
      ...new RptParamsDTO(),
      rptTitleName: this.rptTitleName,
      rptFileName: this.rptFileName,
      rptFormat: this.rptFormat,
      rptPath: '',
      rptJrxml: '',
      rptPS1: startDate,
      rptPS2: endDate,
      rptPS3: departmentID,
      rptPS4: this._loginDepartmentId?.toString(),
      rptPS5: this._lStatus.toString(),
      rptPS6: this.getDepartmentNameById(+departmentID),
      rptPS7: this._logindepartmentName,
      rptPS8: '',
      rptPS9: '',
      rptPS10: '',
    };
  }
}
