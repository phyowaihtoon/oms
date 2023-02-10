import { Component, AfterViewInit, OnInit } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators, FormControl } from '@angular/forms';
import { IMetaDataHeader, MetaDataHeader, IMetaData, MetaData } from '../metadata.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { MetaDataService } from '../service/metadata.service';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LovSetupDialogComponent } from '../lov-setup/lov-setup-dialog.component';
import { IUserAuthority } from 'app/login/userauthority.model';
import { IMenuItem } from 'app/entities/util/setup.model';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { DeleteMetadataComponent } from '../delete-metadata/delete-metadata.component';

@Component({
  selector: 'jhi-metadata-update',
  templateUrl: './metadata-update.component.html',
  styleUrls: ['./metadata-update.component.scss'],
})
export class MetadataUpdateComponent implements OnInit {
  isSaving = false;
  _userAuthority?: IUserAuthority;
  _activeMenuItem?: IMenuItem;

  editForm = this.fb.group({
    id: [],
    docTitle: ['', [Validators.required, Validators.maxLength(100)]],
    fieldList: this.fb.array([]),
  });

  fieldTyepList = [
    { value: 'String', caption: 'String' },
    { value: 'Number', caption: 'Number' },
    { value: 'Date', caption: 'Date' },
    { value: 'LOV', caption: 'LOV' },
  ];

  mendatoryList = [
    { value: 'YES', caption: 'YES' },
    { value: 'NO', caption: 'NO' },
  ];

  dashboardList = [
    { value: 'Y', caption: 'YES' },
    { value: 'N', caption: 'NO' },
  ];

  constructor(
    protected service: MetaDataService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.addField();
    this.activatedRoute.data.subscribe(({ metadata, userAuthority }) => {
      this._userAuthority = userAuthority;
      this._activeMenuItem = userAuthority.activeMenu.menuItem;

      if (metadata !== null) {
        if (metadata.id !== null && metadata.id !== undefined) {
          this.removeAllField();
        }
        this.updateForm(metadata);
      }
    });
  }

  cancel(): void {
    // window.history.back();
    this.editForm.controls['id']!.setValue(undefined);
    this.editForm.controls['docTitle']!.setValue('');
    this.editForm.reset();
    this.removeAllField();
    this.addField();
  }

  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      id: [],
      fieldName: ['', [Validators.required, Validators.maxLength(50), Validators.pattern('^[a-zA-Z ]*$')]],
      fieldType: ['String', [Validators.required]],
      fieldValue: [{ value: '', disabled: true }, Validators.required],
      isRequired: ['YES', [Validators.required]],
      fieldOrder: [this.fieldList().controls.length + 1, [Validators.required, Validators.pattern('^[0-9]*$')]],
      showDashboard: ['N', [Validators.required]],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    if (this.fieldList().length > 1) {
      const id = this.fieldList().controls[i].get(['id'])!.value;
      if (this.fieldList().controls[i].get(['id'])!.value === null || this.fieldList().controls[i].get(['id'])!.value === undefined) {
        this.removeFieldConfirm(i);
      } else {
        const modalRef = this.modalService.open(DeleteMetadataComponent, { size: 'md', backdrop: 'static' });
        // modalRef.componentInstance.id = id;
        modalRef.closed.subscribe(reason => {
          if (reason === 'deleted') {
            this.subscribeToSaveResponseCheckFieldExist(this.service.deleteField(id), i);
          }
        });
      }
    }
  }

  removeFieldConfirm(i: number): void {
    if (this.fieldList().length > 1) {
      this.fieldList().removeAt(i);
      this.reorderFieldList();
    }
  }

  reorderFieldList(): void {
    let index = 1;
    this.fieldList().controls.forEach(data => {
      data.get(['fieldOrder'])!.setValue(index);
      index = index + 1;
    });
  }

  removeAllField(): void {
    this.fieldList().clear();
  }

  onFieldTypeChange(i: any): void {
    if (this.fieldList().controls[i].get(['fieldType'])!.value === 'LOV') {
      this.fieldList().controls[i].get(['fieldValue'])!.enable({ onlySelf: true });
      this.fieldList().controls[i].get(['showDashboard'])!.enable({ onlySelf: true });
    } else {
      this.fieldList().controls[i].get(['fieldValue'])!.setValue('');
      this.fieldList().controls[i].get(['showDashboard'])!.setValue('N');
      this.fieldList().controls[i].get(['fieldValue'])!.disable({ onlySelf: true });
      this.fieldList().controls[i].get(['showDashboard'])!.disable({ onlySelf: true });
    }
  }

  onShowDashboardChange(i: any): void {
    if (this.fieldList().controls[i].get(['showDashboard'])!.value === 'Y') {
      let count = 0;
      this.fieldList().controls.forEach(data => {
        if (data.get(['showDashboard'])!.value === 'Y') {
          count = count + 1;
        }
      });
      if (count > 1) {
        this.fieldList().controls[i].get(['showDashboard'])!.setValue('N');
        const replyCode = ResponseCode.ERROR_E00;
        const replyMsg = 'Only one field can be used to see dashboard.';
        this.showAlertMessage(replyCode, replyMsg);
      }
    }
  }

  addLovValue(i: number): void {
    const val = this.fieldList().controls[i].get(['fieldValue'])!.value;
    // this.fieldList().push(this.newField());
    const modalRef = this.modalService.open(LovSetupDialogComponent, { size: 'md', backdrop: 'static' });
    modalRef.componentInstance.lovStr = this.fieldList().controls[i].get(['fieldValue'])!.value;
    modalRef.componentInstance.fieldName = this.fieldList().controls[i].get(['fieldName'])!.value;
    // unsubscribe not needed because closed completes on modal close
    modalRef.componentInstance.passEntry.subscribe((data: any) => {
      this.fieldList().controls[i].get(['fieldValue'])!.setValue(data);
    });
  }

  save(): void {
    this.isSaving = true;
    const metadata = this.createForm();
    const metadataID = metadata.id ?? undefined;
    if (metadataID !== undefined) {
      this.subscribeToSaveResponse(this.service.update(metadata));
    } else {
      this.subscribeToSaveResponse(this.service.create(metadata));
    }
  }

  showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReplyMessage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(result: HttpResponse<IReplyMessage>): void {
    const replyMessage: IReplyMessage | null = result.body;
    if (replyMessage !== null) {
      this.editForm.get(['id'])?.setValue(replyMessage.data.id);
      const replyCode = replyMessage.code;
      const replyMsg = replyMessage.message;
      this.removeAllField();
      this.updateForm(replyMessage.data);
      this.showAlertMessage(replyCode, replyMsg);
    }
  }

  protected onSaveError(): void {
    console.log('onSaveError');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected subscribeToSaveResponseCheckFieldExist(result: Observable<HttpResponse<IReplyMessage>>, i: number): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccessCheckFieldExist(res, i),
      () => this.onSaveErrorCheckFieldExist()
    );
  }

  protected onSaveSuccessCheckFieldExist(result: HttpResponse<IReplyMessage>, i: number): void {
    const replyMessage: IReplyMessage | null = result.body;

    if (replyMessage !== null) {
      if (replyMessage.code === ResponseCode.ERROR_E00) {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.showAlertMessage(replyCode, replyMsg);
      } else {
        this.removeFieldConfirm(i);
      }
    } else {
      this.onSaveErrorCheckFieldExist();
    }
  }

  protected onSaveErrorCheckFieldExist(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
  }

  protected createMetaDataDetail(data: any): IMetaData {
    return {
      ...new MetaData(),
      id: data.get(['id'])!.value,
      headerId: undefined,
      fieldName: data.get(['fieldName'])!.value,
      fieldType: data.get(['fieldType'])!.value,
      fieldValue: data.get(['fieldValue'])!.value,
      isRequired: data.get(['isRequired'])!.value,
      fieldOrder: data.get(['fieldOrder'])!.value,
      showDashboard: data.get(['showDashboard'])!.value,
      delFlag: 'N',
    };
  }

  protected createMetaDataDetails(): IMetaData[] {
    const fieldList: IMetaData[] = [];
    this.fieldList().controls.forEach(data => {
      fieldList.push(this.createMetaDataDetail(data));
    });
    return fieldList;
  }

  protected createForm(): IMetaDataHeader {
    return {
      ...new MetaDataHeader(),
      id: this.editForm.get(['id'])!.value,
      docTitle: this.editForm.get(['docTitle'])!.value,
      delFlag: 'N',
      metaDataDetails: this.createMetaDataDetails(),
    };
  }

  protected updateForm(metadata: IMetaDataHeader): void {
    this.editForm.patchValue({
      id: metadata.id,
      docTitle: metadata.docTitle,
      fieldList: this.updateMetaDataDetails(metadata.metaDataDetails),
    });
  }

  protected updateMetaDataDetails(metaDataDetails: IMetaData[] | undefined): void {
    let index = 0;
    metaDataDetails?.forEach(data => {
      this.addField();
      this.fieldList().controls[index].get(['id'])!.setValue(data.id);
      this.fieldList().controls[index].get(['fieldName'])!.setValue(data.fieldName);
      this.fieldList().controls[index].get(['fieldType'])!.setValue(data.fieldType);
      this.fieldList().controls[index].get(['fieldValue'])!.setValue(data.fieldValue);
      this.fieldList().controls[index].get(['isRequired'])!.setValue(data.isRequired);
      this.fieldList().controls[index].get(['fieldOrder'])!.setValue(data.fieldOrder);
      this.fieldList().controls[index].get(['showDashboard'])!.setValue(data.showDashboard);
      this.onFieldTypeChange(index);
      index = index + 1;
    });
  }
}
