import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicationUser, ApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IUserRole } from 'app/entities/user-role/user-role.model';
import { UserRoleService } from 'app/entities/user-role/service/user-role.service';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IWorkflowAuthority } from 'app/entities/util/setup.model';
import { IDepartment } from 'app/entities/department/department.model';

@Component({
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  userRolesSharedCollection: IUserRole[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    workflowAuthority: [null, [Validators.required]],
    user: [null, Validators.required],
    userRole: [null, Validators.required],
    department: [],
  });

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected userService: UserService,
    protected userRoleService: UserRoleService,
    protected departmentService: DepartmentService,
    protected loadSetupService: LoadSetupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.updateForm(applicationUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.createFromForm();
    if (applicationUser.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackUserRoleById(index: number, item: IUserRole): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
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

  protected updateForm(applicationUser: IApplicationUser): void {
    this.editForm.patchValue({
      id: applicationUser.id,
      workflowAuthority: applicationUser.workflowAuthority,
      user: applicationUser.user,
      userRole: applicationUser.userRole,
      department: applicationUser.department,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, applicationUser.user);
    this.userRolesSharedCollection = this.userRoleService.addUserRoleToCollectionIfMissing(
      this.userRolesSharedCollection,
      applicationUser.userRole
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.userRoleService
      .query()
      .pipe(map((res: HttpResponse<IUserRole[]>) => res.body ?? []))
      .pipe(
        map((userRoles: IUserRole[]) =>
          this.userRoleService.addUserRoleToCollectionIfMissing(userRoles, this.editForm.get('userRole')!.value)
        )
      )
      .subscribe((userRoles: IUserRole[]) => (this.userRolesSharedCollection = userRoles));

    this.loadSetupService.loadAllSubDepartments().subscribe(res => {
      if (res.body) {
        this.departmentsSharedCollection = res.body;
      }
    });
  }

  protected createFromForm(): IApplicationUser {
    return {
      ...new ApplicationUser(),
      id: this.editForm.get(['id'])!.value,
      workflowAuthority: this.editForm.get(['workflowAuthority'])!.value,
      user: this.editForm.get(['user'])!.value,
      userRole: this.editForm.get(['userRole'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
