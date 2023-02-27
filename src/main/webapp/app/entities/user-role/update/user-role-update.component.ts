import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import {
  IRoleDashboardAccess,
  IRoleMenuAccess,
  IRoleTemplateAccess,
  IUserRole,
  RoleDashboardAccess,
  RoleMenuAccess,
  RoleTemplateAccess,
  UserRole,
} from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';
import { IHeaderDetailsMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { IMetaDataHeader, MetaDataHeader } from 'app/entities/metadata/metadata.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { DashboardTemplate, IDashboardTemplate } from 'app/services/dashboard-template.model';

@Component({
  selector: 'jhi-user-role-update',
  templateUrl: './user-role-update.component.html',
  styleUrls: ['./user-role-update.component.scss'],
})
export class UserRoleUpdateComponent implements OnInit {
  isSaving = false;
  _isMenuAccess = true;
  _isTemplateAccess = false;
  _isDashboardAccess = false;
  _metaDataHdrList?: IMetaDataHeader[];
  _dashboardTemplateList?: IDashboardTemplate[];
  _roleTypes = [
    { value: 0, description: 'NO' },
    { value: 1, description: 'YES' },
  ];

  editForm = this.fb.group({
    id: [],
    roleName: [null, [Validators.required]],
    roleType: [0, [Validators.required]],
    menuAccessList: this.fb.array([]),
    templateAccessList: this.fb.array([]),
    dashboardAccessList: this.fb.array([]),
    metaDataHeader: [],
    dashboardTemplate: [],
    AllowAll: [],
    ReadAll: [],
    WriteAll: [],
    DeleteAll: [],
  });

  constructor(
    protected userRoleService: UserRoleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected loadSetupService: LoadSetupService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ headerDetailsMessage }) => {
      if (headerDetailsMessage.header.id === undefined) {
        this.subscribeToLoadResponse(this.userRoleService.getAllMenuItems());
      }
      this.updateForm(headerDetailsMessage);
    });

    this.loadSetupService.loadAllMetaDataHeader().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        if (res.body) {
          this._metaDataHdrList = res.body;
        }
      },
      error => {
        console.log('Loading MetaData Setup Failed : ', error);
      }
    );

    this.loadSetupService.loadAllDashboardTemplate().subscribe(
      (res: HttpResponse<IDashboardTemplate[]>) => {
        if (res.body) {
          this._dashboardTemplateList = res.body;
        }
      },
      error => {
        console.log('Loading Dashboard Template Failed : ', error);
      }
    );
  }

  get _templateAccessListFCA(): FormArray {
    return this.editForm.get('templateAccessList') as FormArray;
  }

  get _menuAccessListFCA(): FormArray {
    return this.editForm.get('menuAccessList') as FormArray;
  }

  get _dashboardAccessListFCA(): FormArray {
    return this.editForm.get('dashboardAccessList') as FormArray;
  }

  resetForm(): void {
    this.editForm.get('id')?.setValue(undefined);
    this.editForm.get('roleName')?.setValue('');
    this.editForm.get('roleType')?.setValue(0);
    this.editForm.get('AllowAll')?.setValue(false);
    this.editForm.get('ReadAll')?.setValue(false);
    this.editForm.get('WriteAll')?.setValue(false);
    this.editForm.get('DeleteAll')?.setValue(false);
    this._menuAccessListFCA.controls.forEach(rowFormControl => {
      rowFormControl.get('isAllow')?.setValue(false);
      rowFormControl.get('isRead')?.setValue(false);
      rowFormControl.get('isWrite')?.setValue(false);
      rowFormControl.get('isDelete')?.setValue(false);
    });

    this._templateAccessListFCA.clear();
    this._dashboardAccessListFCA.clear();
  }

  onCheckAllMenuAccess(event: any, value: number): void {
    if (event.target.checked) {
      this._menuAccessListFCA.controls.forEach(rowFormControl => {
        if (value === 1) {
          rowFormControl.get('isAllow')?.setValue(true);
        }
        if (value === 2) {
          rowFormControl.get('isRead')?.setValue(true);
        }
        if (value === 3) {
          rowFormControl.get('isWrite')?.setValue(true);
        }
        if (value === 4) {
          rowFormControl.get('isDelete')?.setValue(true);
        }
      });
    }
  }

  initializeNewMenuAccessRow(): void {
    const initialRow = this.fb.group({
      id: [],
      isAllow: [],
      isRead: [],
      isWrite: [],
      isDelete: [],
      menuItem: [],
      userRole: [],
    });
    this._menuAccessListFCA.push(initialRow);
  }

  initializeNewTemplateAccessRow(): void {
    const initialRow = this.fb.group({
      id: [],
      template: [],
      userRole: [],
    });
    this._templateAccessListFCA.push(initialRow);
  }

  addNewTemplateAccessRow(data: IRoleTemplateAccess): void {
    const newTemplateRow = this.fb.group({
      id: [],
      template: [data.metaDataHeader],
      userRole: [data.userRole],
    });
    this._templateAccessListFCA.push(newTemplateRow);
  }

  initializeNewDashboardAccessRow(): void {
    const initialRow = this.fb.group({
      id: [],
      template: [],
      userRole: [],
    });
    this._dashboardAccessListFCA.push(initialRow);
  }

  addNewDashboardAccessRow(data: IRoleDashboardAccess): void {
    const newTemplateRow = this.fb.group({
      id: [],
      template: [data.dashboardTemplate],
      userRole: [data.userRole],
    });
    this._dashboardAccessListFCA.push(newTemplateRow);
  }

  save(): void {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.header.id !== undefined) {
      this.subscribeToSaveResponse(this.userRoleService.update(message));
    } else {
      this.subscribeToSaveResponse(this.userRoleService.create(message));
    }
  }

  switchAccess(id: number): void {
    if (id === 1) {
      this._isMenuAccess = true;
      this._isDashboardAccess = false;
      this._isTemplateAccess = false;
    } else if (id === 2) {
      this._isMenuAccess = false;
      this._isDashboardAccess = false;
      this._isTemplateAccess = true;
    } else {
      this._isMenuAccess = false;
      this._isTemplateAccess = false;
      this._isDashboardAccess = true;
    }
  }

  addTemplate(): void {
    const metaDataHeaderId = +this.editForm.get('metaDataHeader')!.value;
    const addedMetaDataList = this.createTemplateAccess();
    const addedMetaData = addedMetaDataList.find(item => item.metaDataHeader?.id === metaDataHeaderId);
    if (addedMetaData === undefined) {
      const metaDataHeader = this._metaDataHdrList?.find(item => item.id === metaDataHeaderId);
      if (metaDataHeader !== undefined) {
        const roleTemplate = {
          ...new RoleTemplateAccess(),
          metaDataHeader: { ...new MetaDataHeader(), id: metaDataHeader.id, docTitle: metaDataHeader.docTitle },
          userRole: { ...new UserRole(), id: 0, roleName: '' },
        };
        this.addNewTemplateAccessRow(roleTemplate);
      }
    }
    this.editForm.get('metaDataHeader')?.patchValue(0);
  }

  addDashboardTemplate(): void {
    const dashboardTemplateId = +this.editForm.get('dashboardTemplate')!.value;
    const addedDashboardTemplateList = this.createDashboardAccess();
    const addedDashboardTemplate = addedDashboardTemplateList.find(item => item.id === dashboardTemplateId);
    if (addedDashboardTemplate === undefined) {
      const dashboardTemp = this._dashboardTemplateList?.find(item => item.id === dashboardTemplateId);
      if (dashboardTemp !== undefined) {
        const roleDashboard = {
          ...new RoleDashboardAccess(),
          dashboardTemplate: { ...new DashboardTemplate(), id: dashboardTemp.id, cardName: dashboardTemp.cardName },
          userRole: { ...new UserRole(), id: 0, roleName: '' },
        };
        this.addNewDashboardAccessRow(roleDashboard);
      }
    }
    this.editForm.get('dashboardTemplate')?.patchValue(0);
  }

  removeTemplate(index: number): void {
    this._templateAccessListFCA.removeAt(index);
  }

  removeDashboardTemplate(index: number): void {
    this._dashboardAccessListFCA.removeAt(index);
  }

  previousState(): void {
    window.history.back();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHeaderDetailsMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res.body),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(message: IHeaderDetailsMessage | null): void {
    if (message?.code) {
      if (message.code === ResponseCode.SUCCESS) {
        this.editForm.get(['id'])?.setValue(message.header.id);
        this.updateRoleMenuAccess(message.details1);
        this.updateTemplateAccess(message.details2);
        this.updateDashboardAccess(message.details3);
      }
      const replyCode = message.code;
      const replyMsg = message.message;
      this.showAlertMessage(replyCode, replyMsg);
    } else {
      this.onSaveError();
    }
  }

  protected onSaveError(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  protected subscribeToLoadResponse(result: Observable<HttpResponse<IRoleMenuAccess[]>>): void {
    result.subscribe(
      (res: HttpResponse<IRoleMenuAccess[]>) => {
        if (res.body) {
          const roleMenuAccess = res.body;
          this.updateRoleMenuAccess(roleMenuAccess);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  protected updateForm(message: IHeaderDetailsMessage): void {
    const userRole = message.header;
    this.editForm.patchValue({
      id: userRole.id,
      roleName: userRole.roleName,
      roleType: userRole.roleType,
      menuAccessList: this.updateRoleMenuAccess(message.details1),
      templateAccessList: this.updateTemplateAccess(message.details2),
      dashboardAccessList: this.updateDashboardAccess(message.details3),
    });
  }

  protected createFromForm(): IHeaderDetailsMessage {
    return {
      header: {
        ...new UserRole(),
        id: this.editForm.get(['id'])!.value,
        roleName: this.editForm.get(['roleName'])!.value,
        roleType: this.editForm.get(['roleType'])!.value,
      },
      details1: this.createRoleMenuAccess(),
      details2: this.createTemplateAccess(),
      details3: this.createDashboardAccess(),
    };
  }

  protected createRoleMenuAccess(): IRoleMenuAccess[] {
    const fieldList: IRoleMenuAccess[] = [];
    this._menuAccessListFCA.controls.forEach(formControl => {
      fieldList.push(this.getRoleMenuAccess(formControl));
    });
    return fieldList;
  }

  protected getRoleMenuAccess(formControl: any): IRoleMenuAccess {
    return {
      ...new RoleMenuAccess(),
      id: formControl.get(['id'])!.value,
      isAllow: this.convertBoolToNum(formControl.get(['isAllow'])!.value),
      isRead: this.convertBoolToNum(formControl.get(['isRead'])!.value),
      isWrite: this.convertBoolToNum(formControl.get(['isWrite'])!.value),
      isDelete: this.convertBoolToNum(formControl.get(['isDelete'])!.value),
      menuItem: formControl.get(['menuItem'])!.value,
      userRole: formControl.get(['userRole'])!.value,
    };
  }

  protected createTemplateAccess(): IRoleTemplateAccess[] {
    const fieldList: IRoleTemplateAccess[] = [];
    this._templateAccessListFCA.controls.forEach(formControl => {
      fieldList.push(this.getTemplateAccess(formControl));
    });
    return fieldList;
  }

  protected getTemplateAccess(formControl: any): IRoleTemplateAccess {
    return {
      ...new RoleTemplateAccess(),
      id: formControl.get(['id'])!.value,
      metaDataHeader: formControl.get(['template'])!.value,
      userRole: formControl.get(['userRole'])!.value,
    };
  }

  protected updateRoleMenuAccess(menuAccessList: IRoleMenuAccess[] | undefined): void {
    let index = 0;
    this._menuAccessListFCA.clear();
    menuAccessList?.forEach(data => {
      this.initializeNewMenuAccessRow();
      this._menuAccessListFCA.controls[index].get(['id'])!.setValue(data.id);
      this._menuAccessListFCA.controls[index].get(['isAllow'])!.setValue(this.convertNumToBool(data.isAllow));
      this._menuAccessListFCA.controls[index].get(['isRead'])!.setValue(this.convertNumToBool(data.isRead));
      this._menuAccessListFCA.controls[index].get(['isWrite'])!.setValue(this.convertNumToBool(data.isWrite));
      this._menuAccessListFCA.controls[index].get(['isDelete'])!.setValue(this.convertNumToBool(data.isDelete));
      this._menuAccessListFCA.controls[index].get(['menuItem'])!.setValue(data.menuItem);
      this._menuAccessListFCA.controls[index].get(['userRole'])!.setValue(data.userRole);
      index = index + 1;
    });
  }

  protected updateTemplateAccess(templateAccessList: IRoleTemplateAccess[] | undefined): void {
    let index = 0;
    this._templateAccessListFCA.clear();
    templateAccessList?.forEach(data => {
      this.initializeNewTemplateAccessRow();
      this._templateAccessListFCA.controls[index].get(['id'])!.setValue(data.id);
      this._templateAccessListFCA.controls[index].get(['template'])!.setValue(data.metaDataHeader);
      this._templateAccessListFCA.controls[index].get(['userRole'])!.setValue(data.userRole);
      index = index + 1;
    });
  }

  protected createDashboardAccess(): IRoleDashboardAccess[] {
    const fieldList: IRoleDashboardAccess[] = [];
    this._dashboardAccessListFCA.controls.forEach(formControl => {
      fieldList.push(this.getDashboardAccess(formControl));
    });
    return fieldList;
  }

  protected getDashboardAccess(formControl: any): IRoleDashboardAccess {
    return {
      ...new RoleDashboardAccess(),
      id: formControl.get(['id'])!.value,
      dashboardTemplate: formControl.get(['template'])!.value,
      userRole: formControl.get(['userRole'])!.value,
    };
  }

  protected updateDashboardAccess(dashboardAccessList: IRoleDashboardAccess[] | undefined): void {
    let index = 0;
    this._dashboardAccessListFCA.clear();
    dashboardAccessList?.forEach(data => {
      this.initializeNewDashboardAccessRow();
      this._dashboardAccessListFCA.controls[index].get(['id'])!.setValue(data.id);
      this._dashboardAccessListFCA.controls[index].get(['template'])!.setValue(data.dashboardTemplate);
      this._dashboardAccessListFCA.controls[index].get(['userRole'])!.setValue(data.userRole);
      index = index + 1;
    });
  }

  protected convertNumToBool(numValue?: number): boolean {
    if (numValue === 1) {
      return true;
    }
    return false;
  }

  protected convertBoolToNum(boolValue: boolean): number {
    if (boolValue) {
      return 1;
    }
    return 0;
  }
}
