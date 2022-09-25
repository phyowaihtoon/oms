import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMetaData } from '../meta-data.model';

@Component({
  selector: 'jhi-meta-data-detail',
  templateUrl: './meta-data-detail.component.html',
})
export class MetaDataDetailComponent implements OnInit {
  metaData: IMetaData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaData }) => {
      this.metaData = metaData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
