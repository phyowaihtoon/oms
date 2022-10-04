import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';

@Component({
  templateUrl: './user-role-delete-dialog.component.html',
})
export class UserRoleDeleteDialogComponent {
  userRole?: IUserRole;

  constructor(protected userRoleService: UserRoleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userRoleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
