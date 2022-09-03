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
        data: { pageTitle: 'dmsApp.metadata.home.title' },
        loadChildren: () => import('./metadata/metadata.module').then(m => m.MetadataModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'dmsApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'menurole',
        data: { pageTitle: 'dmsApp.menurole.home.title' },
        loadChildren: () => import('./menurole/role-menu-map.module').then(m => m.RoleMenuMapModule),
      },
      {
        path: 'repository',
        data: { pageTitle: 'dmsApp.repository.home.title' },
        loadChildren: () => import('./repository/repository.module').then(m => m.RepositoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
