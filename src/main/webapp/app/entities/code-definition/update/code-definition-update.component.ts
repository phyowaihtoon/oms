import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ICodeDefinition } from '../code-definition.model';
import { CodeDefinitionService } from '../service/code-definition.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-code-definition-update',
  templateUrl: './code-definition-update.component.html',
})
export class CodeDefinitionUpdateComponent implements OnInit {
  isSaving = false;
  codeDefinition: ICodeDefinition | null = null;

  editForm = this.fb.group({});

  constructor(
    protected codeDefinitionService: CodeDefinitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeDefinition }) => {
      this.codeDefinition = codeDefinition;
      if (codeDefinition) {
        this.updateForm(codeDefinition);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    /*
    const codeDefinition = this.codeDefinitionFormService.getCodeDefinition(this.editForm);
    if (codeDefinition.id !== null) {
      this.subscribeToSaveResponse(this.codeDefinitionService.update(codeDefinition));
    } else {
      this.subscribeToSaveResponse(this.codeDefinitionService.create(codeDefinition));
    }
    */
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodeDefinition>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(codeDefinition: ICodeDefinition): void {
    this.codeDefinition = codeDefinition;
  }
}
