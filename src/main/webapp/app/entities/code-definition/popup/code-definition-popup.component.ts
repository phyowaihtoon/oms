import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ICodeDefinition } from '../code-definition.model';
import { CodeDefinitionService } from '../service/code-definition.service';

@Component({
  selector: 'jhi-code-definition-popup',
  templateUrl: './code-definition-popup.component.html',
  styleUrls: ['./code-definition-popup.component.scss'],
})
export class CodeDefinitionPopupComponent implements OnInit {
  _codeDefinitionList?: ICodeDefinition[];
  roleID?: number;

  constructor(protected activeModal: NgbActiveModal, protected codeDefinitionService: CodeDefinitionService) {}

  ngOnInit(): void {
    if (this.roleID !== undefined) {
      this.codeDefinitionService.findCodesByRole(this.roleID).subscribe(
        (res: HttpResponse<ICodeDefinition[]>) => {
          if (res.body) {
            this._codeDefinitionList = res.body;
          }
        },
        () => {
          console.log(' Loading Code Definition failed');
        }
      );
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}
