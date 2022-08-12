import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoleMenuMapRoutingModule } from './route/role-menu-map-routing.module';
import { RoleMenuMapComponent } from './list/role-menu-map.component';
import { RoleMenuMapUpdateComponent } from './update/role-menu-map-update.component';



@NgModule({
  imports: [
    SharedModule,RoleMenuMapRoutingModule
  ],
  declarations: [RoleMenuMapComponent,RoleMenuMapUpdateComponent]
})
export class RoleMenuMapModule { }
