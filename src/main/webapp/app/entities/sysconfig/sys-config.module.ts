import { NgModule } from '@angular/core';
import { SysConfigComponent } from './sys-config.component';
import { SharedModule } from 'app/shared/shared.module';
import { SysConfigRoutingModule } from './sys-config-routing.module';

@NgModule({
  imports: [SharedModule, SysConfigRoutingModule],
  declarations: [SysConfigComponent],
})
export class SysConfigModule {}
