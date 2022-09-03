import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RepositoryUpdateComponent } from '../update/repository-update.component';
import { RepositoryComponent } from '../list/repository.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RepositoryRoutingResolveService } from './repository-routing-resolve.service';

const repositoryRoute: Routes = [
  {
    path: '',
    component: RepositoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'new',
    component: RepositoryUpdateComponent,
    resolve: {
      repository: RepositoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RepositoryUpdateComponent,
    resolve: {
      repository: RepositoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(repositoryRoute)],
  exports: [RouterModule],
})
export class RepositoryRoutingModule {}
