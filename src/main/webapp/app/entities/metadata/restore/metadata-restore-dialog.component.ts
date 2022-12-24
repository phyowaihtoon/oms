import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IMetaDataHeader } from '../metadata.model';
import { MetaDataService } from '../service/metadata.service';

@Component({
  selector: 'jhi-metadata-restore-dialog',
  templateUrl: './metadata-restore-dialog.component.html',
  styleUrls: ['./metadata-restore-dialog.component.scss'],
})
export class MetadataRestoreDialogComponent {
  metadataHeader?: IMetaDataHeader;

  constructor(protected metaDataService: MetaDataService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmRestore(id: number): void {
    this.metaDataService.restoreMetaData(id).subscribe(() => {
      this.activeModal.close('restored');
    });
  }
}
