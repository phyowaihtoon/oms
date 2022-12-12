import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'metadata',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./metadata/metadata.module').then(m => m.MetadataModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'repository',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./repository/repository.module').then(m => m.RepositoryModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
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
        path: 'service-queue',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./servicequeue/service-queue.module').then(m => m.ServiceQueueModule),
      },
      {
        path: 'sysconfig',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./sysconfig/sys-config.module').then(m => m.SysConfigModule),
      },
      {
        path: 'definition',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./code-definition/code-definition.module').then(m => m.CodeDefinitionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
