import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SysConfigComponent } from './sys-config.component';

const sysConfigRoute: Routes = [
  {
    path: '',
    component: SysConfigComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sysConfigRoute)],
  exports: [RouterModule],
})
export class SysConfigRoutingModule {}
