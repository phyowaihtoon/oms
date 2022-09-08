import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRepositoryHeader } from '../repository.model';
import { RepositoryService } from '../service/repository.service';

@Component({
  templateUrl: './repository-delete-dialog.component.html',
})
export class RepositoryDeleteDialogComponent {
  repository?: IRepositoryHeader;

  constructor(protected service: RepositoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
