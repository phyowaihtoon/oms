import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaDataHeader } from '../meta-data-header.model';
import { MetaDataHeaderService } from '../service/meta-data-header.service';

@Component({
  templateUrl: './meta-data-header-delete-dialog.component.html',
})
export class MetaDataHeaderDeleteDialogComponent {
  metaDataHeader?: IMetaDataHeader;

  constructor(protected metaDataHeaderService: MetaDataHeaderService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaDataHeaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
