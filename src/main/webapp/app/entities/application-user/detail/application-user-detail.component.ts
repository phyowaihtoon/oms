import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IWorkflowAuthority } from 'app/entities/util/setup.model';

import { IApplicationUser } from '../application-user.model';

@Component({
  selector: 'jhi-application-user-detail',
  templateUrl: './application-user-detail.component.html',
})
export class ApplicationUserDetailComponent implements OnInit {
  applicationUser: IApplicationUser | null = null;
  workflowAuthorities: IWorkflowAuthority[] = [];

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.applicationUser = applicationUser;
    });
  }

  previousState(): void {
    window.history.back();
  }

  getWorkFlowDescription(key?: number): string {
    const workflow = this.workflowAuthorities.find(item => item.value === key);
    if (workflow?.description) {
      return workflow.description;
    }
    return '';
  }
}
