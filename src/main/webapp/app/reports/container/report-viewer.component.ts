import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReportService } from '../service/report.service';
import { FormBuilder, Validators } from '@angular/forms';
import * as FileSaver from 'file-saver';
@Component({
  selector: 'jhi-report-viewer',
  templateUrl: './report-viewer.component.html',
  styleUrls: ['./report-viewer.component.scss'],
})
export class ReportViewerComponent implements OnInit {
  isDownloading = false;
  pdfBlobURL: Blob = new Blob();
  rptTitleName: string | null = '';
  rptFileName: string | null = '';

  editForm = this.fb.group({
    rptTitleName: [],
    rptFileName: [],
    rptFormat: [null, [Validators.required]],
  });

  constructor(private reportService: ReportService, protected fb: FormBuilder, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.rptTitleName = this.activatedRoute.snapshot.queryParamMap.get('rptTitleName');
    this.rptFileName = this.activatedRoute.snapshot.queryParamMap.get('rptFileName');
    const rptFormat = this.activatedRoute.snapshot.queryParamMap.get('rptFormat');
    if (this.rptFileName != null && rptFormat != null) {
      const rptFileWithExt = this.rptFileName.concat(rptFormat.toString());
      this.reportService.showPDF(rptFileWithExt).subscribe((res: Blob) => {
        this.pdfBlobURL = res;
        console.log("PDF Byte Data from server");
        console.log(this.pdfBlobURL);
      },
      error => console.log("Failed to show file in report viewer",error));
    }
  }

  download(): void {
    const rptFormat = this.editForm.get(['rptFormat'])!.value;
    const rptFileNameWithExt = this.rptFileName?.concat(rptFormat);
    if (rptFileNameWithExt !== undefined) {
      this.reportService.downloadFile(rptFileNameWithExt).subscribe(
        (res: Blob) => {
          FileSaver.saveAs(res, rptFileNameWithExt);
          // const downloadURL=window.URL.createObjectURL(res);
          // window.open(downloadURL);
        }
      );
    }
  }
}
