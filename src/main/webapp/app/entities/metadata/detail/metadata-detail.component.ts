import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMetaDataHeader } from '../metadata.model';

@Component({
  selector: 'jhi-metadata-detail',
  templateUrl: './metadata-detail.component.html',
  styleUrls: ['./metadata-detail.component.scss'],
})
export class MetaDataDetailComponent implements OnInit {
  metadata: IMetaDataHeader | null = null;

  _isShow = true;
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metadata }) => {
      this.metadata = metadata;
      console.log('metadata data =>' + JSON.stringify(this.metadata));
    });
  }

  previousState(): void {
    window.history.back();
  }

  showData(): void {
    if (this._isShow === false) {
      this._isShow = !this._isShow;
    }

    this._isShow = false;
  }
}
