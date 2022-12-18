import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IRepositoryHeader } from '../repository.model';
import { RepositoryService } from '../service/repository.service';

@Component({
  selector: 'jhi-repository-restore-dialog',
  templateUrl: './repository-restore-dialog.component.html',
  styleUrls: ['./repository-restore-dialog.component.scss'],
})
export class RepositoryRestoreDialogComponent {
  repository?: IRepositoryHeader;

  constructor(protected repositoryService: RepositoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmRestore(id: number): void {
    this.repositoryService.restoreRepository(id).subscribe(() => {
      this.activeModal.close('restored');
    });
  }
}
