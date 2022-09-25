import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoleMenuMap } from '../role-menu-map.model';
import { RoleMenuMapService } from '../service/role-menu-map.service';
import { RoleMenuMapDeleteDialogComponent } from '../delete/role-menu-map-delete-dialog.component';

@Component({
  selector: 'jhi-role-menu-map',
  templateUrl: './role-menu-map.component.html',
})
export class RoleMenuMapComponent implements OnInit {
  roleMenuMaps?: IRoleMenuMap[];
  isLoading = false;

  constructor(protected roleMenuMapService: RoleMenuMapService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.roleMenuMapService.query().subscribe(
      (res: HttpResponse<IRoleMenuMap[]>) => {
        this.isLoading = false;
        this.roleMenuMaps = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRoleMenuMap): number {
    return item.id!;
  }

  delete(roleMenuMap: IRoleMenuMap): void {
    const modalRef = this.modalService.open(RoleMenuMapDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.roleMenuMap = roleMenuMap;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
