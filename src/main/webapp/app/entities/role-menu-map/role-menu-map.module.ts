import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RoleMenuMapComponent } from './list/role-menu-map.component';
import { RoleMenuMapDetailComponent } from './detail/role-menu-map-detail.component';
import { RoleMenuMapUpdateComponent } from './update/role-menu-map-update.component';
import { RoleMenuMapDeleteDialogComponent } from './delete/role-menu-map-delete-dialog.component';
import { RoleMenuMapRoutingModule } from './route/role-menu-map-routing.module';

@NgModule({
  imports: [SharedModule, RoleMenuMapRoutingModule],
  declarations: [RoleMenuMapComponent, RoleMenuMapDetailComponent, RoleMenuMapUpdateComponent, RoleMenuMapDeleteDialogComponent],
  entryComponents: [RoleMenuMapDeleteDialogComponent],
})
export class RoleMenuMapModule {}
