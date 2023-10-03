import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import {
  DeliveryMessage,
  DocumentAttachment,
  DocumentDelivery,
  DocumentReceiver,
  IDeliveryMessage,
  IDocumentAttachment,
  IDocumentDelivery,
  IDocumentReceiver,
} from 'app/entities/delivery/delivery.model';
import { DeliveryService } from 'app/entities/delivery/service/delivery.service';
import { Department, HeadDepartment, IDepartment, IHeadDepartment } from 'app/entities/department/department.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { IMeetingDelivery, IMeetingMessage, MeetingDelivery, MeetingMessage } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-meeting-update',
  templateUrl: './meeting-update.component.html',
  styleUrls: ['./meeting-update.component.scss'],
})
export class MeetingUpdateComponent implements OnInit {
  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;

  editForm = this.fb.group({
    id: [],
    location: ['', [Validators.required]],
    fromtime: [null, [Validators.required]],
    totime: [null, [Validators.required]],
    meetingDate: [null, [Validators.required]],
    subject: ['', [Validators.required]],
    body: ['', [Validators.required]],
    mDeptList: [],
    cDeptList: [],
    docList: this.fb.array([]),
    cc_location: ['', [Validators.required]],
    cc_body: ['', [Validators.required]],
    cc_subject: ['', [Validators.required]],
    ccfromtime: [null, [Validators.required]],
    cctotime: [null, [Validators.required]],
    ccmeetingDate: [null, [Validators.required]],
  });

  public progressStep = 1;
  toLabel = 'To:';
  ccLabel = 'Cc:';
  toDepartments?: IDepartment[] = [];
  ccDepartments?: IDepartment[] = [];
  modules = {};

  public progressItems = [
    { step: 1, title: 'Info' },
    { step: 2, title: 'Receiver' },
    { step: 3, title: 'Attachment' },
    { step: 4, title: 'Delivery' },
  ];

  _modalRef?: NgbModalRef;
  _tempdocList: File[] = [];
  name = 'Progress Bar';
  isSaving = false;
  isInfo = true;
  isReceiver = false;
  isAttachment = false;
  isSuccess = false;
  public counts = ['Info', 'Receiver', 'Attachment', 'Success'];

  public status = 'Info';

  constructor(
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected loadSetupService: LoadSetupService,
    protected meetingService: MeetingService,
    protected translateService: TranslateService
  ) {
    this.editForm.controls.location.valueChanges.subscribe(value => {
      //   // Update the targetText control's value
      this.editForm.controls.cc_location.setValue(value);
    });
    this.editForm.controls.subject.valueChanges.subscribe(value => {
      // Update the targetText control's value
      this.editForm.controls.cc_subject.setValue(value);
    });
    this.editForm.controls.body.valueChanges.subscribe(value => {
      // Update the targetText control's value
      this.editForm.controls.cc_body.setValue(value);
    });
    this.editForm.controls.meetingDate.valueChanges.subscribe(value => {
      // Update the targetText control's value
      this.editForm.controls.ccmeetingDate.setValue(value);
    });

    this.editForm.controls.fromtime.valueChanges.subscribe(value => {
      // Update the targetText control's value
      this.editForm.controls.ccfromtime.setValue(value);
    });
    this.editForm.controls.totime.valueChanges.subscribe(value => {
      // Update the targetText control's value
      this.editForm.controls.cctotime.setValue(value);
    });

    this.modules = {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'], // toggled buttons
        ['blockquote', 'code-block'],

        [{ color: [] }, { background: [] }], // dropdown with defaults from theme
        [{ font: [] }],
        [{ align: [] }],
      ],
    };
  }

  ngOnInit(): void {
    this.progressItems[0].title = this.translateService.instant('global.menu.meeting.Step1');
    this.progressItems[1].title = this.translateService.instant('global.menu.meeting.Step2');
    this.progressItems[2].title = this.translateService.instant('global.menu.meeting.Step3');
    this.progressItems[3].title = this.translateService.instant('global.menu.meeting.Step4');

    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
      this.progressItems[0].title = this.translateService.instant('global.menu.meeting.Step1');
      this.progressItems[1].title = this.translateService.instant('global.menu.meeting.Step2');
      this.progressItems[2].title = this.translateService.instant('global.menu.meeting.Step3');
      this.progressItems[3].title = this.translateService.instant('global.menu.meeting.Step4');
    });
  }
  // Demo purpose only, Data might come from Api calls/service

  goToStep1(): void {
    this.progressStep = 1;
    this.isInfo = true;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isSuccess = false;
  }

  goToStep2(): void {
    this.progressStep = 2;
    this.isInfo = false;
    this.isReceiver = true;
    this.isAttachment = false;
    this.isSuccess = false;
  }

  goToStep3(): void {
    this.progressStep = 3;
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = true;
    this.isSuccess = false;
  }

  goToStep4(): void {
    this.progressStep = 4;
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isSuccess = true;
  }

  docList(): FormArray {
    return this.editForm.get('docList') as FormArray;
  }
  checkFormArrayEmpty(): boolean {
    const formArray = this.editForm.get('docList') as FormArray;
    console.log(formArray.length, 'DocList');

    if (formArray.length === 0) {
      return true;
    } else {
      return false;
    }
  }
  onToDepartmentChange(event: any): void {
    this.toDepartments = event;
    console.log(this.toDepartments, 'to Dept');
  }

  onCcDepartmentChange(event: any): void {
    this.ccDepartments = event;
    console.log(this.ccDepartments, 'cc Dept');
  }

  submit(): void {
    console.log('##### Save #####');
  }

  // create new field dynamically
  newField(filePath: string, fileName: string, fileData?: File): FormGroup {
    return this.fb.group({
      id: [],
      filePath: [filePath, [Validators.required]],
      fileName: [fileName, [Validators.required]],
      fileData: [fileData],
    });
  }

  // add new field dynamically
  addField(filePath: string, fileName: string, fileData?: File): void {
    this.docList().push(this.newField(filePath, fileName, fileData));
  }

  removeFieldConfirm(i: number): void {
    this.docList().removeAt(i);
    this.myInputVariable!.nativeElement.value = '';
  }

  // remove all Field of document table
  removeAllField(): void {
    this.docList().clear();
  }

  removeField(i: number): void {
    const docId = this.docList().controls[i].get(['id'])!.value;
    const dmsFileName = this.docList().controls[i].get(['fileName'])!.value;

    if (this.docList().controls[i].get(['id'])!.value === null || this.docList().controls[i].get(['id'])!.value === undefined) {
      this.removeFieldConfirm(i);
    } else {
      console.log('xxx');
    }
  }

  checkRepositoryURL(inputFileElement: HTMLInputElement): void {
    inputFileElement.click();
  }

  // define a function to upload files
  onUploadFiles(event: Event): void {
    const target = event.target as HTMLInputElement;

    for (let i = 0; i <= target.files!.length - 1; i++) {
      const selectedFile = target.files![i];
      this._tempdocList.push(selectedFile);
    }

    for (let i = 0; i < this._tempdocList.length; i++) {
      const tempFile = this._tempdocList[i];
      this.addField('c://', tempFile.name, tempFile);
    }

    this._tempdocList = [];
    this.myInputVariable!.nativeElement.value = '';
  }

  save(deliveryStatus: number): void {
    this.showLoading('Saving Documents in Draft');
    this.isSaving = true;
    this.showLoading('Saving and Uploading Documents');

    const formData = new FormData();
    const attacheddocList = [];
    const meetingDelivery = this.createFrom(deliveryStatus);

    console.log(meetingDelivery, 'xxxDocumentDeliveryxxx');

    const docList = meetingDelivery.attachmentList ?? [];

    if (docList.length > 0) {
      for (const dmsDoc of docList) {
        const docDetailID = dmsDoc.id ?? undefined;
        if (docDetailID === undefined && dmsDoc.fileData !== undefined) {
          const orgFileName = dmsDoc.fileData.name;
          const orgFileExtension = orgFileName.split('.').pop();
          let editedFileName = dmsDoc.fileName;
          if (editedFileName?.lastIndexOf('.') === -1) {
            // If file extension is missing, original file extension is added
            editedFileName = editedFileName.concat('.').concat(orgFileExtension!);
            dmsDoc.fileName = editedFileName;
          }
          const docDetailInfo = editedFileName
            ?.concat('@')
            .concat(dmsDoc.filePath ?? '')
            .concat('@');
          formData.append('files', dmsDoc.fileData, docDetailInfo);
        }
        delete dmsDoc['fileData'];
        attacheddocList.push(dmsDoc);
      }
    }

    this.subscribeToSaveResponse(this.meetingService.save(formData, meetingDelivery));
  }

  showLoading(loadingMessage?: string): void {
    this._modalRef = this.modalService.open(LoadingPopupComponent, { size: 'sm', backdrop: 'static', centered: true });
    this._modalRef.componentInstance.loadingMessage = loadingMessage;
    this.hideLoading();
  }

  showAlertMessage(msg1: string, msg2?: string): void {
    const modalRef = this.modalService.open(InfoPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
    modalRef.componentInstance.code = msg1;
    modalRef.componentInstance.message = msg2;
  }

  hideLoading(): void {
    this._modalRef?.close();
  }

  protected createFrom(deliveryStatus: number): IMeetingMessage {
    return {
      ...new MeetingMessage(),

      meetingDelivery: this.createFormMetingDelivery(deliveryStatus),
      receiverList: this.createFormReceiverList(),
      attachmentList: this.createFormdocList(),
    };
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
      if (replyMessage.code === ResponseCode.SUCCESS) {
        this.editForm.get(['id'])?.setValue(replyMessage.data.id);

        console.log(replyMessage.data, 'xxx Reply Message xxx');

        this.removeAllField();
        this.updateForm(replyMessage.data);
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.showAlertMessage(replyCode, replyMsg);
      } else {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.showAlertMessage(replyCode, replyMsg);
      }
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
    this.hideLoading();
  }

  protected createFormMetingDelivery(deliveryStatusPara: number): IMeetingDelivery {
    const format = 'YYYY-MM-DD';
    const m_date = dayjs(this.editForm.get(['meetingDate'])!.value, { format });
    const s_date = dayjs(String(this.editForm.get(['meetingDate'])!.value) + String(this.editForm.get(['fromtime'])!.value), { format });
    const e_date = dayjs(String(this.editForm.get(['meetingDate'])!.value) + String(this.editForm.get(['totime'])!.value), { format });

    return {
      ...new MeetingDelivery(),

      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['id'])!.value,
      sentDate: undefined,
      startDate: s_date,
      endDate: e_date,
      place: this.editForm.get(['location'])!.value,
      subject: this.editForm.get(['subject'])!.value,
      description: this.editForm.get(['body'])!.value,
      deliveryStatus: deliveryStatusPara,
      meetingStatus: 1,
      status: 1,
      delFlag: 'N',
      sender: undefined,
    };
  }

  protected createFormReceiverList(): IDocumentReceiver[] {
    const receiverList: IDocumentReceiver[] = [];

    this.toDepartments?.forEach((value, index) => {
      receiverList.push(this.createReceiverListDetail(1, value));
    });

    this.ccDepartments?.forEach((value, index) => {
      receiverList.push(this.createReceiverListDetail(2, value));
    });

    return receiverList;
  }

  protected createReceiverListDetail(receiver_Type: number, iDepartment: IDepartment): IDocumentReceiver {
    return {
      ...new DocumentReceiver(),
      id: undefined,
      receiverType: receiver_Type,
      status: 0,
      delFlag: 'N',
      receiver: iDepartment,
    };
  }

  protected getDepartment(): IDepartment {
    return {
      ...new Department(),
      id: 1,
      departmentName: 'အမြဲတမ်းအတွင်းဝန်ရုံးခန်း',
      delFlag: 'N',
      headDepartment: this.getHeadDepartment(),
    };
  }

  protected getHeadDepartment(): IHeadDepartment {
    return {
      ...new HeadDepartment(),
      id: 1,
      description: 'ပြည်ထောင်စုတရားသူကြီးချုပ်ရုံး',
    };
  }

  protected createFormdocList(): IDocumentAttachment[] {
    const fieldList: IDocumentAttachment[] = [];
    this.docList().controls.forEach(data => {
      fieldList.push(this.createdocListDetail(data));
    });
    return fieldList;
  }

  protected createdocListDetail(data: any): IDocumentAttachment {
    return {
      ...new DocumentAttachment(),
      id: data.get(['id'])!.value,
      filePath: data.get(['filePath'])!.value,
      fileName: data.get(['fileName'])!.value,
      delFlag: 'N',      
      fileData: data.get(['fileData'])!.value,
    };
  }

  protected updateForm(deliveryMessage: IDeliveryMessage): void {
    this.updateDocDelivery(deliveryMessage.documentDelivery!);
    this.updateDocDetails(deliveryMessage.attachmentList);
    this.editForm.patchValue({
      docList: this.updateDocDetails(deliveryMessage.attachmentList),
    });
  }

  protected updateDocDelivery(docDelivery: IDocumentDelivery): void {
    this.editForm.patchValue({
      id: docDelivery.id,
      docNo: docDelivery.referenceNo,
      subject: docDelivery.subject,
      body: docDelivery.description,
    });
  }

  protected updateDocDetails(docList: IDocumentAttachment[] | undefined): void {
    let index = 0;
    docList?.forEach(data => {
      this.addField('', '');
      this.docList().controls[index].get(['fileName'])!.setValue(data.fileName);
      index = index + 1;
    });
  }
}
