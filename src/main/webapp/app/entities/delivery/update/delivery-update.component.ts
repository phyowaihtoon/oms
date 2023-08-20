import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup} from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingPopupComponent } from 'app/entities/util/loading/loading-popup.component';
import { DocumentDelivery, IDocumentDelivery } from '../delivery.model';
import { Department, IDepartment } from './data.model';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { DeliveryService } from '../service/delivery.service';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { InfoPopupComponent } from 'app/entities/util/infopopup/info-popup.component';

@Component({
  selector: 'jhi-delivery-update',
  templateUrl: './delivery-update.component.html',
  styleUrls: ['./delivery-update.component.scss'],
})
export class DeliveryUpdateComponent {

  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;
       
  editForm = this.fb.group({
    id: [],
    docNo:['', [Validators.required]],
    subject:['', [Validators.required]],
    body:['', [Validators.required]],
    mDeptList: [],
    cDeptList: [],
    fileList: this.fb.array([]),
    cc_docNo: ['', [Validators.required]],
    cc_body: ['', [Validators.required]],
    cc_subject: ['', [Validators.required]],

  });

  _modalRef?: NgbModalRef;
  name = 'Progress Bar';
  isSaving = false;
  isInfo = true;
  isReceiver = false;
  isAttachment = false;
  isSuccess = false;
  public counts = ["Info","Receiver","Attachment","Success"];
  senderDept!: IDepartment;

  public status = "Info"

  constructor(
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected loadSetupService: LoadSetupService,
    protected deliveryService: DeliveryService,
  ) {

    this.editForm.controls.docNo.valueChanges.subscribe((value) => {
      // Update the targetText control's value
      this.editForm.controls.cc_docNo.setValue(value);
    });
    this.editForm.controls.subject.valueChanges.subscribe((value) => {
      // Update the targetText control's value
      this.editForm.controls.cc_subject.setValue(value);
    });
    this.editForm.controls.body.valueChanges.subscribe((value) => {
      // Update the targetText control's value
      this.editForm.controls.cc_body.setValue(value);
    });
  }
  

  // Demo purpose only, Data might come from Api calls/service 

  goToStep1(): void{
    this.status = "Info"
    this.isInfo = true;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isSuccess = false;
  }

  goToStep2(): void{
    this.status = "Receiver"
    this.isInfo = false;
    this.isReceiver = true;
    this.isAttachment = false;
    this.isSuccess = false;
  }

  goToStep3(): void{
    this.status = "Attachment"
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = true;
    this.isSuccess = false;
  }

  goToStep4(): void{
    this.status = "Success"
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isSuccess = true;
  }

  submit():void{
    console.log("##### Save #####")
  }

  fileList(): FormArray {
    return this.editForm.get('fileList') as FormArray;
  }

   // create new field dynamically
   newField(filePath: string, fileName: string, fileSize: number, fileData?: File): FormGroup {
    return this.fb.group({
      id: [],
      filePath: [filePath, [Validators.required]],
      fileName: [fileName, [Validators.required]],
      fileNameVersion: [],
      fileSize: [fileSize, [Validators.required]],
      version: [''],
      remark: [''],
      fileData: [fileData],
    });
  }

   // add new field dynamically
   addField(filePath: string, fileName: string, fileSize: number, fileData?: File): void {
    this.fileList().push(this.newField(filePath, fileName, fileSize, fileData));
  }

  
  removeFieldConfirm(i: number): void {
    this.fileList().removeAt(i);
    this.myInputVariable!.nativeElement.value = '';
  }

  // remove all Field of document table
  removeAllField(): void {
    this.fileList().clear();
  }
  
  removeField(i: number): void {
      const docId = this.fileList().controls[i].get(['id'])!.value;
      const dmsFileName = this.fileList().controls[i].get(['fileName'])!.value;

      if (this.fileList().controls[i].get(['id'])!.value === null || this.fileList().controls[i].get(['id'])!.value === undefined) {
        this.removeFieldConfirm(i);
      } else {
        console.log("xxx");
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
    }

  }
  saveDraft(): void {    
    console.log("Step1");
    this.showLoading('Saving Documents in Draft');
    this.isSaving = true;
    this.showLoading('Saving and Uploading Documents');
    
    const formData = new FormData();
    const attachedFileList = [];
    const documentDelivery = this.createFrom();
    formData.append('delivery', JSON.stringify(documentDelivery));
    this.subscribeToSaveResponse(this.deliveryService.save(formData));

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

  protected createFrom(): IDocumentDelivery {
    return {
      ...new DocumentDelivery(),
      id: this.editForm.get(['id'])!.value,
      referenceNo: this.editForm.get(['docNo'])!.value,
      subject: this.editForm.get(['subject'])!.value,
      description: this.editForm.get(['body'])!.value,
      deliveryStatus: 1,
      status: 1,
      delFlag: 'N',
      sender:   undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReplyMessage>>): void {
    
    console.log("Step3");
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

        console.log("Success Save");

        this.removeAllField();

        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
      } else {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
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
}
