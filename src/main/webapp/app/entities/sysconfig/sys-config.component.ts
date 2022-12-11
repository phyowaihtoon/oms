import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { finalize } from 'rxjs/operators';
import { InfoPopupComponent } from '../util/infopopup/info-popup.component';
import { IReplyMessage, ResponseCode } from '../util/reply-message.model';
import { ISysConfig, SysConfig } from './sys-config.model';
import { SysConfigService } from './sys-config.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-sys-config',
  templateUrl: './sys-config.component.html',
  styleUrls: ['./sys-config.component.scss'],
})
export class SysConfigComponent implements OnInit {
  isSaving = false;
  _sysConfigList?: ISysConfig[];

  editForm = this.fb.group({
    configArray: this.fb.array([]),
  });

  constructor(protected sysConfigService: SysConfigService, protected fb: FormBuilder, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.sysConfigService.loadAllSysConfig().subscribe(
      (res: HttpResponse<IReplyMessage>) => {
        if (res.body) {
          this._sysConfigList = res.body.data;
          this.updateFrom(this._sysConfigList);
        }
      },
      error => {
        console.log('Loading System Config failed :', error);
      }
    );
  }

  get configArray(): FormArray {
    return this.editForm.get('configArray') as FormArray;
  }

  initializeNewSysConfigRow(): void {
    const initialRow = this.fb.group({
      id: [],
      code: [],
      value: [],
      enabled: [],
      definition: [],
    });
    this.configArray.push(initialRow);
  }

  save(): void {
    this.isSaving = true;
    const message = this.createSysConfig();
    this.subscribeToSaveResponse(this.sysConfigService.update(message));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReplyMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res.body),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(message: IReplyMessage | null): void {
    if (message?.code) {
      if (message.code === ResponseCode.SUCCESS) {
        this.updateFrom(message.data);
      }
      const replyCode = message.code;
      const replyMsg = message.message;
      this.showAlertMessage(replyCode, replyMsg);
    } else {
      this.onSaveError();
    }
  }

  protected onSaveError(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createSysConfig(): ISysConfig[] {
    const configList: ISysConfig[] = [];
    this.configArray.controls.forEach(formControl => {
      configList.push(this.getSysConfig(formControl));
    });
    return configList;
  }

  protected getSysConfig(formControl: any): ISysConfig {
    return {
      ...new SysConfig(),
      id: formControl.get(['id'])!.value,
      code: formControl.get(['code'])!.value,
      value: formControl.get(['value'])!.value,
      enabled: this.convertBoolToFlag(formControl.get(['enabled'])!.value),
      definition: formControl.get(['definition'])!.value,
    };
  }

  protected showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
    modalRef.componentInstance.logoutFlag = true;
  }

  protected updateFrom(sysConfigs: ISysConfig[] | undefined): void {
    let index = 0;
    this.configArray.clear();
    sysConfigs?.forEach(data => {
      this.initializeNewSysConfigRow();
      this.configArray.controls[index].get(['id'])!.setValue(data.id);
      this.configArray.controls[index].get(['code'])!.setValue(data.code);
      this.configArray.controls[index].get(['value'])!.setValue(data.value);
      this.configArray.controls[index].get(['enabled'])!.setValue(this.convertFlagToBool(data.enabled));
      this.configArray.controls[index].get(['definition'])!.setValue(data.definition);
      index = index + 1;
    });
  }

  protected convertFlagToBool(numValue?: string): boolean {
    if (numValue === 'Y') {
      return true;
    }
    return false;
  }

  protected convertBoolToFlag(boolValue: boolean): string {
    if (boolValue) {
      return 'Y';
    }
    return 'N';
  }
}
