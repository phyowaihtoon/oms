import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaData } from '../meta-data.model';
import { MetaDataService } from '../service/meta-data.service';

@Component({
  templateUrl: './meta-data-delete-dialog.component.html',
})
export class MetaDataDeleteDialogComponent {
  metaData?: IMetaData;

  constructor(protected metaDataService: MetaDataService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
