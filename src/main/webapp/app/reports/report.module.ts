import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportViewerComponent } from './container/report-viewer.component';
import { ReportFormRoutingModule } from './route/report-form-routing.module';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { DoclistRptComponent } from './doclist-rpt/doclist-rpt.component';

@NgModule({
  imports: [SharedModule, ReportFormRoutingModule],
  declarations: [ReportViewerComponent, LoadingPopupComponent, DoclistRptComponent],
})
export class ReportModule {}
