import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { DocumentDetailComponent } from '../detail/document-detail.component';
import { DocumentComponent } from '../list/document.component';
import { DocumentUpdateComponent } from '../update/document-update.component';
import { DocumentRoutingResolveService } from './document-routing-resolve.service';

const metadataRoute: Routes = [
  {
    path: 'new',
    component: DocumentUpdateComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'DOCMC',
    },
  },
  {
    path: 'list',
    component: DocumentComponent,
    resolve: {
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'DOCMI',
    },
  },
  {
    path: ':id/view',
    component: DocumentDetailComponent,
    resolve: {
      docHeader: DocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentUpdateComponent,
    resolve: {
      docHeader: DocumentRoutingResolveService,
      userAuthority: UserAuthorityResolveService,
    },
    data: {
      menuCode: 'DOCMC',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(metadataRoute)],
  exports: [RouterModule],
})
export class DocumentRoutingModule {}
