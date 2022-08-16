import { Component, OnInit } from '@angular/core';
import { IDocumentInquiry } from '../document.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss'],
})
export class DocumentComponent implements OnInit {
  documentHeaders?: IDocumentInquiry[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  ngOnInit(): void {
    this.documentHeaders = [
      { id: 1, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
      { id: 2, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
      { id: 3, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
      { id: 4, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
      { id: 5, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
      { id: 6, docTitle: 'PaySlip', repositoryURL: 'payslip/2022' },
    ];
  }

  trackId(index: number, item: IDocumentInquiry): number {
    return item.id!;
  }
}
