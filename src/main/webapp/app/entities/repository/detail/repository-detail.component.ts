import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRepository } from '../repository.model';

@Component({
  selector: 'jhi-repository-detail',
  templateUrl: './repository-detail.component.html',
})
export class RepositoryDetailComponent implements OnInit {
  repository: IRepository | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repository }) => {
      this.repository = repository;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
