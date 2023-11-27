import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { ReportViewerComponent } from '../container/report-viewer.component';
import { DoclistRptComponent } from '../doclist-rpt/doclist-rpt.component';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'report-viewer',
        component: ReportViewerComponent,
      },
      {
        path: 'doclist-rpt',
        component: DoclistRptComponent,
        resolve: {
          userAuthority: UserAuthorityResolveService,
        },
        data: {
          menuCode: 'DOCLR',
        },
      },
    ]),
  ],
})
export class ReportFormRoutingModule {}
