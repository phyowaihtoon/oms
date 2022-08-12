import { NgModule } from '@angular/core';
import { RoleMenuMapComponent } from '../list/role-menu-map.component';
import { RouterModule,Routes } from '@angular/router';

const roleMenuMapRoute: Routes = [
  {
    path: '',
    component: RoleMenuMapComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(roleMenuMapRoute)],
  exports: [RouterModule],
})
export class RoleMenuMapRoutingModule { }
