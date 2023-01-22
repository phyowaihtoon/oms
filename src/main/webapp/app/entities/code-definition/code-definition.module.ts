import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CodeDefinitionComponent } from './list/code-definition.component';
import { CodeDefinitionDetailComponent } from './detail/code-definition-detail.component';
import { CodeDefinitionUpdateComponent } from './update/code-definition-update.component';
import { CodeDefinitionDeleteDialogComponent } from './delete/code-definition-delete-dialog.component';
import { CodeDefinitionRoutingModule } from './route/code-definition-routing.module';
import { CodeDefinitionPopupComponent } from './popup/code-definition-popup.component';

@NgModule({
  imports: [SharedModule, CodeDefinitionRoutingModule],
  declarations: [
    CodeDefinitionComponent,
    CodeDefinitionDetailComponent,
    CodeDefinitionUpdateComponent,
    CodeDefinitionDeleteDialogComponent,
    CodeDefinitionPopupComponent,
  ],
})
export class CodeDefinitionModule {}
