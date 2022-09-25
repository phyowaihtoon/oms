import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoleMenuMap } from '../role-menu-map.model';

@Component({
  selector: 'jhi-role-menu-map-detail',
  templateUrl: './role-menu-map-detail.component.html',
})
export class RoleMenuMapDetailComponent implements OnInit {
  roleMenuMap: IRoleMenuMap | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roleMenuMap }) => {
      this.roleMenuMap = roleMenuMap;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
