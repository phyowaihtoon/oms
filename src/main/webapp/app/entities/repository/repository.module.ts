import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RepositoryRoutingModule } from './route/repository-routing.module';
import { RepositoryUpdateComponent } from './update/repository-update.component';
import { RepositoryComponent } from './list/repository.component';
import { RepositoryDeleteDialogComponent } from './delete/repository-delete-dialog.component';

@NgModule({
  imports: [SharedModule, RepositoryRoutingModule],
  declarations: [RepositoryComponent, RepositoryComponent, RepositoryUpdateComponent, RepositoryDeleteDialogComponent],
  entryComponents: [RepositoryDeleteDialogComponent],
})
export class RepositoryModule {}
