import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRepository, IRepositoryHeader } from '../repository.model';

@Component({
  selector: 'jhi-repository-detail',
  templateUrl: './repository-detail.component.html',
  styleUrls: ['./repository-detail.component.scss'],
})
export class RepositoryDetailComponent implements OnInit {
  repository: IRepositoryHeader | null = null;

  _isShow = true;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repository }) => {
      this.repository = repository;
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
