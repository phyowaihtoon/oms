import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'dmsApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'metadata',
        data: { pageTitle: 'dmsApp.metaData.home.title' },
        loadChildren: () => import('./metadata/metadata.module').then(m => m.MetadataModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'dmsApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'repository',
        data: { pageTitle: 'dmsApp.repository.home.title' },
        loadChildren: () => import('./repository/repository.module').then(m => m.RepositoryModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'dmsApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'dmsApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      {
        path: 'user-role',
        data: { pageTitle: 'dmsApp.userRole.home.title' },
        loadChildren: () => import('./user-role/user-role.module').then(m => m.UserRoleModule),
      },
      {
        path: 'service-queue',
        data: { pageTitle: 'dmsApp.userRole.home.title' },
        loadChildren: () => import('./servicequeue/service-queue.module').then(m => m.ServiceQueueModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
