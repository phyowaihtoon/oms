import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaDataHeader } from '../metadata.model';
import { MetaDataService } from '../service/metadata.service';

@Component({
  templateUrl: './metadata-delete-dialog.component.html',
})
export class MetaDataDeleteDialogComponent {
  metadata?: IMetaDataHeader;

  constructor(protected service: MetaDataService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
