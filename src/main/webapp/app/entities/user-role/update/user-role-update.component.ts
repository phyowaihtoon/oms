import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoleMenuAccess, IUserRole, RoleMenuAccess, UserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';
import { IHeaderDetailsMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';

@Component({
  selector: 'jhi-user-role-update',
  templateUrl: './user-role-update.component.html',
  styleUrls: ['./user-role-update.component.scss'],
})
export class UserRoleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    roleName: [null, [Validators.required]],
    menuAccessList: this.fb.array([]),
  });

  constructor(
    protected userRoleService: UserRoleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ headerDetailsMessage }) => {
      if (headerDetailsMessage.header.id === undefined) {
        this.subscribeToLoadResponse(this.userRoleService.getAllMenuItems());
      }
      this.updateForm(headerDetailsMessage);
    });
  }

  get _menuAccessListFCA(): FormArray {
    return this.editForm.get('menuAccessList') as FormArray;
  }

  resetForm(): void {
    this.editForm.get('id')?.setValue(undefined);
    this.editForm.get('roleName')?.setValue('');
    this._menuAccessListFCA.controls.forEach(rowFormControl => {
      rowFormControl.get('isAllow')?.setValue(false);
      rowFormControl.get('isRead')?.setValue(false);
      rowFormControl.get('isWrite')?.setValue(false);
      rowFormControl.get('isDelete')?.setValue(false);
    });
  }

  initializeNewRow(): void {
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

  save(): void {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.header.id !== undefined) {
      this.subscribeToSaveResponse(this.userRoleService.update(message));
    } else {
      this.subscribeToSaveResponse(this.userRoleService.create(message));
    }
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
        this.updateRoleMenuAccess(message.details);
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
      menuAccessList: this.updateRoleMenuAccess(message.details),
    });
  }

  protected createFromForm(): IHeaderDetailsMessage {
    return {
      header: {
        ...new UserRole(),
        id: this.editForm.get(['id'])!.value,
        roleName: this.editForm.get(['roleName'])!.value,
      },
      details: this.createRoleMenuAccess(),
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

  protected updateRoleMenuAccess(menuAccessList: IRoleMenuAccess[] | undefined): void {
    let index = 0;
    this._menuAccessListFCA.clear();
    menuAccessList?.forEach(data => {
      this.initializeNewRow();
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
