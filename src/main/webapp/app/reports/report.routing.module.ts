import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'report',
        data: { pageTitle: 'home.title' },
        loadChildren: () => import('./report.module').then(m => m.ReportModule),
      },
    ]),
  ],
})
export class ReporRoutingtModule {}
