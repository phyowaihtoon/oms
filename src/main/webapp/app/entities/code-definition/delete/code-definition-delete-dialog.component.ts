import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeDefinition } from '../code-definition.model';
import { CodeDefinitionService } from '../service/code-definition.service';

@Component({
  templateUrl: './code-definition-delete-dialog.component.html',
})
export class CodeDefinitionDeleteDialogComponent {
  codeDefinition?: ICodeDefinition;

  constructor(protected codeDefinitionService: CodeDefinitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codeDefinitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
