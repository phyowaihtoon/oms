import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMetaDataHeader } from '../meta-data-header.model';
import { MetaDataHeaderService } from '../service/meta-data-header.service';
import { MetaDataHeaderDeleteDialogComponent } from '../delete/meta-data-header-delete-dialog.component';

@Component({
  selector: 'jhi-meta-data-header',
  templateUrl: './meta-data-header.component.html',
})
export class MetaDataHeaderComponent implements OnInit {
  metaDataHeaders?: IMetaDataHeader[];
  isLoading = false;

  constructor(protected metaDataHeaderService: MetaDataHeaderService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.metaDataHeaderService.query().subscribe(
      (res: HttpResponse<IMetaDataHeader[]>) => {
        this.isLoading = false;
        this.metaDataHeaders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  delete(metaDataHeader: IMetaDataHeader): void {
    const modalRef = this.modalService.open(MetaDataHeaderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.metaDataHeader = metaDataHeader;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
