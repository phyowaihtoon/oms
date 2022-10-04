import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUserRole, UserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';

@Component({
  selector: 'jhi-user-role-update',
  templateUrl: './user-role-update.component.html',
})
export class UserRoleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    roleName: [null, [Validators.required]],
  });

  constructor(protected userRoleService: UserRoleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userRole }) => {
      this.updateForm(userRole);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userRole = this.createFromForm();
    if (userRole.id !== undefined) {
      this.subscribeToSaveResponse(this.userRoleService.update(userRole));
    } else {
      this.subscribeToSaveResponse(this.userRoleService.create(userRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserRole>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userRole: IUserRole): void {
    this.editForm.patchValue({
      id: userRole.id,
      roleName: userRole.roleName,
    });
  }

  protected createFromForm(): IUserRole {
    return {
      ...new UserRole(),
      id: this.editForm.get(['id'])!.value,
      roleName: this.editForm.get(['roleName'])!.value,
    };
  }
}
