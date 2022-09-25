import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleMenuMap } from '../role-menu-map.model';
import { RoleMenuMapService } from '../service/role-menu-map.service';

@Component({
  templateUrl: './role-menu-map-delete-dialog.component.html',
})
export class RoleMenuMapDeleteDialogComponent {
  roleMenuMap?: IRoleMenuMap;

  constructor(protected roleMenuMapService: RoleMenuMapService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleMenuMapService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
