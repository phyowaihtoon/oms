import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaData } from '../meta-data.model';
import { MetaDataService } from '../service/meta-data.service';
import { MetaDataDeleteDialogComponent } from '../delete/meta-data-delete-dialog.component';

@Component({
  selector: 'jhi-meta-data',
  templateUrl: './meta-data.component.html',
})
export class MetaDataComponent implements OnInit {
  metaData?: IMetaData[];
  isLoading = false;

  constructor(protected metaDataService: MetaDataService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.metaDataService.query().subscribe(
      (res: HttpResponse<IMetaData[]>) => {
        this.isLoading = false;
        this.metaData = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMetaData): number {
    return item.id!;
  }

  delete(metaData: IMetaData): void {
    const modalRef = this.modalService.open(MetaDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.metaData = metaData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
