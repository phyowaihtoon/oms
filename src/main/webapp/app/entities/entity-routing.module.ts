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
        path: 'meta-data-header',
        data: { pageTitle: 'dmsApp.metaDataHeader.home.title' },
        loadChildren: () => import('./meta-data-header/meta-data-header.module').then(m => m.MetaDataHeaderModule),
      },
      {
        path: 'meta-data',
        data: { pageTitle: 'dmsApp.metaData.home.title' },
        loadChildren: () => import('./meta-data/meta-data.module').then(m => m.MetaDataModule),
      },
      {
        path: 'document-header',
        data: { pageTitle: 'dmsApp.documentHeader.home.title' },
        loadChildren: () => import('./document-header/document-header.module').then(m => m.DocumentHeaderModule),
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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
