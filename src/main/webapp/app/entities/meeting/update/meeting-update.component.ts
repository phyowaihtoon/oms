import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { Department, HeadDepartment, IDepartment, IHeadDepartment } from 'app/entities/department/department.model';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import {
  IMeetingAttachment,
  IMeetingDelivery,
  IMeetingMessage,
  IMeetingReceiver,
  MeetingAttachment,
  MeetingDelivery,
  MeetingMessage,
  MeetingReceiver,
} from '../meeting.model';
import { MeetingService } from '../service/meeting.service';
import * as dayjs from 'dayjs';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { DocumentDeleteDialogComponent } from '../delete/document-delete-dialog/document-delete-dialog.component';
import { ConfirmPopupComponent } from 'app/entities/util/confirm-popup/confirm-popup.component';
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
    referenceno: ['', [Validators.required]],
    fromtime: [null, [Validators.required]],
    totime: [null, [Validators.required]],
    meetingDate: [null, [Validators.required]],
    subject: ['', [Validators.required]],
    body: ['', [Validators.required]],
    mDeptList: [],
    cDeptList: [],
    docList: this.fb.array([]),
    cc_location: ['', [Validators.required]],
    cc_referenceno: ['', [Validators.required]],
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
  _departmentName: string | undefined = '';

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
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router,
    protected modalService: NgbModal,
    protected loadSetupService: LoadSetupService,
    protected meetingService: MeetingService,
    protected translateService: TranslateService,
    protected userAuthorityService: UserAuthorityService
  ) {
    this.editForm.controls.referenceno.valueChanges.subscribe(value => {
      //   // Update the targetText control's value
      this.editForm.controls.cc_referenceno.setValue(value);
    });
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
      const selectedDate = value.format('DD-MM-YYYY');
      this.editForm.controls.ccmeetingDate.setValue(selectedDate);
    });

    this.editForm.controls.fromtime.valueChanges.subscribe(value => {
      // Split the hours and minutes from the selected time
      const [hours, minutes] = value.split(':').map(Number);

      // Create a new Date object with the selected time
      const selectedTime = new Date();
      selectedTime.setHours(hours);
      selectedTime.setMinutes(minutes);

      // Add 1 hour to the selected time
      selectedTime.setHours(selectedTime.getHours() + 1);

      // Format the new time as 'HH:mm'
      const newTime = `${selectedTime.getHours().toString().padStart(2, '0')}:${selectedTime.getMinutes().toString().padStart(2, '0')}`;

      // Update the totime control's value with the new time
      this.editForm.controls.totime.setValue(newTime);
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
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;

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

    this.activatedRoute.data.subscribe(({ meeting }) => {
      this.updateForm(meeting);
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
    if (formArray.length === 0) {
      return true;
    } else {
      return false;
    }
  }
  onToDepartmentChange(event: any): void {
    this.toDepartments = event;
  }

  onCcDepartmentChange(event: any): void {
    this.ccDepartments = event;
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
    const docFileName = this.docList().controls[i].get(['fileName'])!.value;

    if (this.docList().controls[i].get(['id'])!.value === null || this.docList().controls[i].get(['id'])!.value === undefined) {
      this.removeFieldConfirm(i);
    } else {
      const dmsDocument = { ...new MeetingAttachment(), id: docId, fileName: docFileName };
      const modalRef = this.modalService.open(DocumentDeleteDialogComponent, { size: 'md', backdrop: 'static' });
      modalRef.componentInstance.dmsDocument = dmsDocument;
      modalRef.componentInstance.confirmMessage.subscribe((confirmed: string) => {
        if (confirmed && confirmed === 'YES') {
          this.subscribeToSaveResponseCheckFileexist(this.meetingService.deleteAttachment(docId), i);
        }
      });
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
      this.addField('', tempFile.name, tempFile);
    }

    this._tempdocList = [];
    this.myInputVariable!.nativeElement.value = '';
  }

  confirmSave(deliveryStatus: number): void {
    if (deliveryStatus === 0) {
      this.showLoading('Saving Meeting Draft');
      this.save(deliveryStatus);
    }
    if (deliveryStatus === 1) {
      const modalRef = this.modalService.open(ConfirmPopupComponent, { size: 'lg', backdrop: 'static', centered: true });
      modalRef.componentInstance.actionMessage.subscribe((confirmed: string) => {
        if (confirmed && confirmed === 'CONFIRM') {
          this.showLoading('Sending Meeting Invitation');
          this.save(deliveryStatus);
        }
      });
    }
  }

  save(deliveryStatus: number): void {
    this.isSaving = true;
    const formData = new FormData();
    const attacheddocList = [];
    const meetingDelivery = this.createFrom(deliveryStatus);

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
    const meetingID = meetingDelivery.meetingDelivery!.id ?? undefined;
    if (meetingID !== undefined) {
      this.subscribeToSaveResponse(this.meetingService.update(formData, meetingDelivery, meetingID));
    } else {
      this.subscribeToSaveResponse(this.meetingService.save(formData, meetingDelivery));
    }
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
    modalRef.componentInstance.successMessage.subscribe((confirmed: string) => {
      if (confirmed && confirmed === ResponseCode.SUCCESS) {
        this.router.navigate(['']);
      }
    });
  }

  hideLoading(): void {
    this._modalRef?.close();
  }

  protected subscribeToSaveResponseCheckFileexist(result: Observable<HttpResponse<IReplyMessage>>, i: number): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccessCheckFileexist(res, i),
      () => this.onSaveErrorCheckFileexist()
    );
  }

  protected onSaveSuccessCheckFileexist(result: HttpResponse<IReplyMessage>, i: number): void {
    const replyMessage: IReplyMessage | null = result.body;

    if (replyMessage !== null) {
      if (replyMessage.code === ResponseCode.ERROR_E00) {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        // this.showAlertMessage(replyCode, replyMsg);
        this.removeFieldConfirm(i);
      } else {
        this.removeFieldConfirm(i);
      }
    } else {
      this.onSaveErrorCheckFileexist();
    }
  }

  protected onSaveErrorCheckFileexist(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    this.showAlertMessage(replyCode, replyMsg);
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
        const meetingMessage: IMeetingMessage = replyMessage.data;
        this.removeAllField();
        this.updateMeetingDetails(meetingMessage.attachmentList);
        if (meetingMessage.receiverList) {
          this.updateReceiverList(meetingMessage.receiverList);
        }
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
    const meetingSetDate = this.editForm.get(['meetingDate'])!.value.format('YYYY-MM-DD');
    const s_date = dayjs(String(meetingSetDate) + String(this.editForm.get(['fromtime'])!.value));
    const e_date = dayjs(String(meetingSetDate) + String(this.editForm.get(['totime'])!.value));

    return {
      ...new MeetingDelivery(),
      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['referenceno'])!.value,
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

  protected createFormReceiverList(): IMeetingReceiver[] {
    const receiverList: IMeetingReceiver[] = [];

    this.toDepartments?.forEach((value, index) => {
      receiverList.push(this.createReceiverListDetail(1, value));
    });

    this.ccDepartments?.forEach((value, index) => {
      receiverList.push(this.createReceiverListDetail(2, value));
    });

    return receiverList;
  }

  protected createReceiverListDetail(receiver_Type: number, iDepartment: IDepartment): IMeetingReceiver {
    return {
      ...new MeetingReceiver(),
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

  protected createFormdocList(): IMeetingAttachment[] {
    const fieldList: IMeetingAttachment[] = [];
    this.docList().controls.forEach(data => {
      fieldList.push(this.createdocListDetail(data));
    });
    return fieldList;
  }

  protected createdocListDetail(data: any): IMeetingAttachment {
    return {
      ...new MeetingAttachment(),
      id: data.get(['id'])!.value,
      filePath: data.get(['filePath'])!.value,
      fileName: data.get(['fileName'])!.value,
      delFlag: 'N',
      fileData: data.get(['fileData'])!.value,
    };
  }

  protected updateForm(meetingMessage: IMeetingMessage): void {
    this.updateMeetingDelivery(meetingMessage.meetingDelivery!);
    this.updateReceiverList(meetingMessage.receiverList!);

    this.editForm.patchValue({
      docList: this.updateMeetingDetails(meetingMessage.attachmentList),
    });
  }

  protected updateMeetingDelivery(meetingDelivery: IMeetingDelivery): void {
    this.editForm.patchValue({
      id: meetingDelivery.id,
      meetingDate: meetingDelivery.startDate,
      fromtime: meetingDelivery.startDate?.format('HH:mm:ss'),
      totime: meetingDelivery.endDate?.format('HH:mm:ss'),
      location: meetingDelivery.place,
      referenceno: meetingDelivery.referenceNo,
      subject: meetingDelivery.subject,
      body: meetingDelivery.description,
    });
  }

  protected updateMeetingDetails(docList: IMeetingAttachment[] | undefined): void {
    let index = 0;
    docList?.forEach(data => {
      this.addField('', '');
      this.docList().controls[index].get(['id'])!.setValue(data.id);
      this.docList().controls[index].get(['fileName'])!.setValue(data.fileName);
      this.docList().controls[index].get(['filePath'])!.setValue(data.filePath);
      index = index + 1;
    });
  }

  protected updateReceiverList(receiverList: IMeetingReceiver[]): void {
    this.toDepartments = [];
    this.ccDepartments = [];
    receiverList.forEach((value, index) => {
      if (value.receiverType === 1) {
        this.toDepartments?.push(value.receiver!);
      } else {
        this.ccDepartments?.push(value.receiver!);
      }
    });
  }
}
