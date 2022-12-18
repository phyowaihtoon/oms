import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICodeDefinition } from '../code-definition.model';

@Component({
  selector: 'jhi-code-definition-detail',
  templateUrl: './code-definition-detail.component.html',
})
export class CodeDefinitionDetailComponent implements OnInit {
  codeDefinition: ICodeDefinition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeDefinition }) => {
      this.codeDefinition = codeDefinition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
