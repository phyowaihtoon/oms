import { Component } from '@angular/core';
import { FormBuilder,Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IRptParamsDTO, RptParamsDTO } from '../report.model';
import { ReportService } from '../service/report.service';

@Component({
  selector: 'jhi-docmapping-rpt',
  templateUrl: './docmapping-rpt.component.html',
  styleUrls: ['./docmapping-rpt.component.scss']
})
export class DocMappingRptComponent {
  isGenerating = false;
  rptParams: IRptParamsDTO | null = null;
  testData: IRptParamsDTO | null = null;
  rptTitleName: string = 'Document Mapping Report';
  rptFileName: string = 'DocumentMappingRpt';
  rptFormat: string = '.pdf';


  editForm = this.fb.group({
    rptTitleName: [],
    rptFileName: [],
    rptFormat: [],
    startDate:[null,[Validators.required]],
    endDate:[null,[Validators.required]],
  });

  constructor(private reportService: ReportService, protected fb: FormBuilder, protected router: Router) {}

  generate(): void {
    this.isGenerating = true;
    const reportData = this.createFromForm();
    this.reportService.generateDocMappingListRpt(reportData).subscribe(
      res => {
        this.rptParams = res.body;
        if (this.rptParams != null) {
          this.router.navigate(['report/report-viewer'], {
            queryParams: {
              rptTitleName: this.rptParams.rptTitleName,
              rptFileName: this.rptParams.rptFileName,
              rptFormat: this.rptParams.rptFormat,
            },
          });
        }
      }
    );
  }

  protected createFromForm(): IRptParamsDTO {
    const startDate=this.editForm.get(['startDate'])!.value.format("DD-MM-YYYY");
    const endDate=this.editForm.get(['endDate'])!.value.format("DD-MM-YYYY");
    return {
      ...new RptParamsDTO(),
      rptTitleName: this.rptTitleName,
      rptFileName: this.rptFileName,
      rptFormat: this.rptFormat,
      rptPath: '',
      rptJrxml: '',
      rptPS1: startDate,
      rptPS2: endDate,
      rptPS3: '',
      rptPS4: '',
      rptPS5: '',
    };
  }

}
