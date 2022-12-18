import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CodeDefinitionComponent } from '../list/code-definition.component';
import { CodeDefinitionDetailComponent } from '../detail/code-definition-detail.component';
import { CodeDefinitionUpdateComponent } from '../update/code-definition-update.component';
import { CodeDefinitionRoutingResolveService } from './code-definition-routing-resolve.service';

const codeDefinitionRoute: Routes = [
  {
    path: '',
    component: CodeDefinitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CodeDefinitionDetailComponent,
    resolve: {
      codeDefinition: CodeDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CodeDefinitionUpdateComponent,
    resolve: {
      codeDefinition: CodeDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CodeDefinitionUpdateComponent,
    resolve: {
      codeDefinition: CodeDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(codeDefinitionRoute)],
  exports: [RouterModule],
})
export class CodeDefinitionRoutingModule {}
