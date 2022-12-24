import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RepositoryUpdateComponent } from '../update/repository-update.component';
import { RepositoryComponent } from '../list/repository.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RepositoryRoutingResolveService } from './repository-routing-resolve.service';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { RepositoryDetailComponent } from '../detail/repository-detail.component';
import { RepositoryTrashbinComponent } from '../trashbin/repository-trashbin.component';

const repositoryRoute: Routes = [
  {
    path: '',
    component: RepositoryComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      defaultSort: 'id,asc',
      menuCode: 'REPOI',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'new',
    component: RepositoryUpdateComponent,
    resolve: {
      repository: RepositoryRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'REPOC',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RepositoryUpdateComponent,
    resolve: {
      repository: RepositoryRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'REPOC',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RepositoryDetailComponent,
    resolve: {
      repository: RepositoryRoutingResolveService,
    }
    },
    {
    path: 'trashbin',
    component: RepositoryTrashbinComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      defaultSort: 'id,asc',
      menuCode: 'REPOTB',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(repositoryRoute)],
  exports: [RouterModule],
})
export class RepositoryRoutingModule {}
