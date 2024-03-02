import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'department',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'announcement',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./announcement/announcement.module').then(m => m.AnnouncementModule),
      },
      {
        path: 'delivery',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./delivery/delivery.module').then(m => m.DeliveryModule),
      },
      {
        path: 'meeting',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./meeting/meeting.module').then(m => m.MeetingModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      {
        path: 'user-role',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./user-role/user-role.module').then(m => m.UserRoleModule),
      },
      {
        path: 'sysconfig',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./sysconfig/sys-config.module').then(m => m.SysConfigModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
