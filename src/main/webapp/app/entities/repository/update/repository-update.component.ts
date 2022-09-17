import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators, FormControl } from '@angular/forms';
import { IRepositoryHeader, RepositoryHeader, IRepository, Repository } from '../repository.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { RepositoryService } from '../service/repository.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-repository-update',
  templateUrl: './repository-update.component.html',
  styleUrls: ['./repository-update.component.scss'],
})
export class RepositoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    repositoryName: ['', [Validators.required]],
    fieldList: this.fb.array([]),
  });

  constructor(protected service: RepositoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.addField();
    this.activatedRoute.data.subscribe(({ repository }) => {
      if (repository !== null) {
        if (repository.id !== null && repository.id !== undefined) {
          this.removeAllField();
        }
        this.updateForm(repository);
      }
    });
  }

  cancel(): void {
    // window.history.back();
    this.editForm.controls['id']!.setValue(undefined);
    this.editForm.controls['repositoryName']!.setValue('');
    this.removeAllField();
    this.addField();
  }

  /* previousState(): void {
    window.history.back();
  }
 */
  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      folderName: ['', [Validators.required]],
      folderOrder: [''],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    if (this.fieldList().length > 1) {
      this.fieldList().controls.splice(i);
    }
  }

  removeAllField(): void {
    this.fieldList().clear();
  }

  save(): void {
    this.isSaving = true;
    const repository = this.createFromForm();
    console.log(JSON.stringify(repository));
    if (repository.id !== undefined) {
      this.subscribeToSaveResponse(this.service.update(repository));
    } else {
      this.subscribeToSaveResponse(this.service.create(repository));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRepositoryHeader>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(result: HttpResponse<IRepositoryHeader>): void {
    this.editForm.get(['id'])?.setValue(result.body?.id);
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createRepositoryDetail(data: any, index: number): IRepository {
    return {
      ...new Repository(),
      id: undefined,
      headerId: undefined,
      folderName: data.get(['folderName'])!.value,
      folderOrder: index,
      delFlag: 'N',
    };
  }

  protected createRepositoryDetails(): IRepository[] {
    const fieldList: IRepository[] = [];
    let index = 0;
    this.fieldList().controls.forEach(data => {
      index = index + 1;
      fieldList.push(this.createRepositoryDetail(data, index));
    });
    return fieldList;
  }

  protected createFromForm(): IRepositoryHeader {
    return {
      ...new RepositoryHeader(),
      id: this.editForm.get(['id'])!.value,
      repositoryName: this.editForm.get(['repositoryName'])!.value,
      delFlag: 'N',
      repositoryDetails: this.createRepositoryDetails(),
    };
  }

  protected updateForm(repository: IRepositoryHeader): void {
    this.editForm.patchValue({
      id: repository.id,
      repositoryName: repository.repositoryName,
      fieldList: this.updateRepositoryDetails(repository.repositoryDetails),
    });
  }

  protected updateRepositoryDetails(metaDataDetails: IRepository[] | undefined): void {
    // this.removeAllField();
    let index = 0;
    metaDataDetails?.forEach(data => {
      this.addField();
      this.fieldList().controls[index].get(['folderName'])!.setValue(data.folderName);
      index = index + 1;
    });
  }
}
