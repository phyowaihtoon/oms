import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDepartment, Department, IHeadDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { LoadSetupService } from 'app/entities/util/load-setup.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;

  headDepartmentList: IHeadDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    departmentName: [null, [Validators.required]],
    delFlag: [],
    headDepartment: [],
  });

  constructor(
    protected departmentService: DepartmentService,
    protected loadSetupService: LoadSetupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    
    this.activatedRoute.data.subscribe(({ department }) => {
      this.updateForm(department);
    });

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
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.createFromForm();
    if (department.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  trackDepartmentById(index: number, item: IHeadDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  protected updateForm(department: IDepartment): void {
    this.editForm.patchValue({
      id: department.id,
      departmentName: department.departmentName,
      delFlag: department.delFlag,
      headDepartment: department.headDepartment,
    });
  }

  protected createFromForm(): IDepartment {
    return {
      ...new Department(),
      id: this.editForm.get(['id'])!.value,
      departmentName: this.editForm.get(['departmentName'])!.value,
      delFlag: 'N',
      headDepartment: this.editForm.get(['headDepartment'])!.value,
    };
  }
}
