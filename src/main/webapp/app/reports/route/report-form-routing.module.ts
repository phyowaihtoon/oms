import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReportViewerComponent } from '../container/report-viewer.component';
import { DocMappingRptComponent } from '../docmapping-rpt/docmapping-rpt.component';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'report-viewer',
        component: ReportViewerComponent,
      },
      {
        path: 'docmapping-rpt',
        component: DocMappingRptComponent,
      },
    ]),
  ],
})
export class ReportFormRoutingModule {}
