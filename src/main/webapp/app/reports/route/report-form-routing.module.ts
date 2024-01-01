import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserAuthorityResolveService } from 'app/login/user-authority-resolve.service';
import { ReportViewerComponent } from '../container/report-viewer.component';
import { DoclistRptComponent } from '../doclist-rpt/doclist-rpt.component';
import { OutLetterRptComponent } from '../outletter-rpt/outletter-rpt.component';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'report-viewer',
        component: ReportViewerComponent,
      },
      {
        path: 'inletter-rpt',
        component: DoclistRptComponent,
        resolve: {
          userAuthority: UserAuthorityResolveService,
        },
        data: {
          menuCode: 'INCRPT',
        },
      },
      {
        path: 'outletter-rpt',
        component: OutLetterRptComponent,
        resolve: {
          userAuthority: UserAuthorityResolveService,
        },
        data: {
          menuCode: 'OUTRPT',
        },
      },
    ]),
  ],
})
export class ReportFormRoutingModule {}
