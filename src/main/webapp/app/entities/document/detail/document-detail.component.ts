import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { IDocument, IDocumentHeader } from '../document.model';

@Component({
  selector: 'jhi-document-detail',
  templateUrl: './document-detail.component.html',
  styleUrls: ['./document-detail.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DocumentDetailComponent implements OnInit {
  _documentHeader: IDocumentHeader = {};
  _documentDetails: IDocument[] = [];
  _docExtensionTypes = [
    { extension: 'pdf', value: 'PDF' },
    { extension: 'docx', value: 'WORD' },
    { extension: 'xls', value: 'EXCEL' },
    { extension: 'xlsx', value: 'EXCEL' },
    { extension: 'jpg', value: 'JPG' },
    { extension: 'txt', value: 'TEXT' },
    { extension: 'csv', value: 'CSV' },
  ];

  ngOnInit(): void {
    this._documentHeader = {
      id: 1,
      metaDataHeaderId: 2,
      fieldNames: 'Open Date|Account Number|Address|Age',
      fieldValues: '01-02-2022|10000000123|Hlaing Township, Yangon, Myanmar|20',
      repositoryURL: 'example/abc',
    };
    this._documentDetails = [
      { id: 1, headerId: 1, filePath: 'example/abc/test.pdf' },
      { id: 2, headerId: 1, filePath: 'example/abc/test.xls' },
      { id: 3, headerId: 1, filePath: 'example/abc/test.jpg' },
      { id: 4, headerId: 1, filePath: 'example/abc/test.docx' },
      { id: 5, headerId: 1, filePath: 'example/abc/test.pdf' },
      { id: 6, headerId: 1, filePath: 'example/abc/test.pdf' },
      { id: 6, headerId: 1, filePath: 'example/abc/test.html' },
    ];
  }

  getFileType(fileName?: string): string | undefined {
    let fileType = '';
    if (fileName !== undefined) {
      const fileExtension = fileName.split('.').pop();
      if (fileExtension === undefined || fileExtension.trim().length === 0) {
        return fileType;
      }
      const docExtensionType = this._docExtensionTypes.find(item => item.extension === fileExtension);
      if (docExtensionType) {
        fileType = docExtensionType.value;
      } else {
        fileType = fileExtension;
      }
    }
    return fileType;
  }

  arrangeMetaData(fNames?: string, fValues?: string): string {
    let arrangedFields = '';
    if (fNames !== undefined && fValues !== undefined && fNames.trim().length > 0 && fValues.trim().length > 0) {
      const fNameArray = fNames.split('|');
      const fValueArray = fValues.split('|');
      if (fNameArray.length > 0 && fValueArray.length > 0 && fNameArray.length === fValueArray.length) {
        let arrIndex = 0;
        while (arrIndex < fNameArray.length) {
          const rowStart = "<div class='row col-12'>";
          const col_1_Start = "<div class='col-2'>";
          const col_1_Data = '<span>' + fNameArray[arrIndex] + '</span>';
          const col_1_End = '</div>';
          const col_2_Start = "<div class='col-4 dms-view-data'>";
          const col_2_Data = '<span>' + fValueArray[arrIndex] + '</span>';
          const col_2_End = '</div>';
          const rowEnd = '</div>';
          arrangedFields += rowStart + col_1_Start + col_1_Data + col_1_End + col_2_Start + col_2_Data + col_2_End + rowEnd;
          arrIndex++;
        }
      }
    }
    return arrangedFields;
  }

  previousState(): void {
    window.history.back();
  }
}
