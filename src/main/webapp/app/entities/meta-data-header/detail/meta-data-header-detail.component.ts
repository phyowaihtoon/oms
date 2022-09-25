import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMetaDataHeader } from '../meta-data-header.model';

@Component({
  selector: 'jhi-meta-data-header-detail',
  templateUrl: './meta-data-header-detail.component.html',
})
export class MetaDataHeaderDetailComponent implements OnInit {
  metaDataHeader: IMetaDataHeader | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaDataHeader }) => {
      this.metaDataHeader = metaDataHeader;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
