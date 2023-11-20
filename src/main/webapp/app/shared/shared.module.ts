import { NgModule } from '@angular/core';
import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { DepartmentPopupComponent } from 'app/entities/util/departementpopup/department-popup.component';
import { DepartmentCustomComponent } from 'app/entities/util/departement-custom-component/department-custom.component';
import { TruncateNumberPipe } from './truncate/truncate-number.pipe';
import { TruncateTextPipe } from './truncate/truncate-text.pipe';

@NgModule({
  imports: [SharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    TruncateNumberPipe,
    TruncateTextPipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    InfoPopupComponent,
    DepartmentPopupComponent,
    DepartmentCustomComponent,
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    TruncateNumberPipe,
    TruncateTextPipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    InfoPopupComponent,
    DepartmentPopupComponent,
    DepartmentCustomComponent,
  ],
})
export class SharedModule {}
