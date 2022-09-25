import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentHeaderComponent } from '../list/document-header.component';
import { DocumentHeaderDetailComponent } from '../detail/document-header-detail.component';
import { DocumentHeaderUpdateComponent } from '../update/document-header-update.component';
import { DocumentHeaderRoutingResolveService } from './document-header-routing-resolve.service';

const documentHeaderRoute: Routes = [
  {
    path: '',
    component: DocumentHeaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentHeaderDetailComponent,
    resolve: {
      documentHeader: DocumentHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentHeaderUpdateComponent,
    resolve: {
      documentHeader: DocumentHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentHeaderUpdateComponent,
    resolve: {
      documentHeader: DocumentHeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentHeaderRoute)],
  exports: [RouterModule],
})
export class DocumentHeaderRoutingModule {}
