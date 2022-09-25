import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRoleMenuMap, RoleMenuMap } from '../role-menu-map.model';
import { RoleMenuMapService } from '../service/role-menu-map.service';

@Component({
  selector: 'jhi-role-menu-map-update',
  templateUrl: './role-menu-map-update.component.html',
})
export class RoleMenuMapUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    roleId: [null, [Validators.required]],
    menuId: [null, [Validators.required]],
    allowed: [null, [Validators.required, Validators.maxLength(1)]],
  });

  constructor(protected roleMenuMapService: RoleMenuMapService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roleMenuMap }) => {
      this.updateForm(roleMenuMap);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roleMenuMap = this.createFromForm();
    if (roleMenuMap.id !== undefined) {
      this.subscribeToSaveResponse(this.roleMenuMapService.update(roleMenuMap));
    } else {
      this.subscribeToSaveResponse(this.roleMenuMapService.create(roleMenuMap));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoleMenuMap>>): void {
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

  protected updateForm(roleMenuMap: IRoleMenuMap): void {
    this.editForm.patchValue({
      id: roleMenuMap.id,
      roleId: roleMenuMap.roleId,
      menuId: roleMenuMap.menuId,
      allowed: roleMenuMap.allowed,
    });
  }

  protected createFromForm(): IRoleMenuMap {
    return {
      ...new RoleMenuMap(),
      id: this.editForm.get(['id'])!.value,
      roleId: this.editForm.get(['roleId'])!.value,
      menuId: this.editForm.get(['menuId'])!.value,
      allowed: this.editForm.get(['allowed'])!.value,
    };
  }
}
