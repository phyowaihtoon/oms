import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportViewerComponent } from './container/report-viewer.component';
import { ReportFormRoutingModule } from './route/report-form-routing.module';
import { DocMappingRptComponent } from './docmapping-rpt/docmapping-rpt.component';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';

@NgModule({
  imports: [SharedModule, ReportFormRoutingModule],
  declarations: [ReportViewerComponent, DocMappingRptComponent, LoadingPopupComponent],
})
export class ReportModule {}
