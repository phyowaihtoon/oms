import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup} from '@angular/forms';
import { IData } from './data.model';

@Component({
  selector: 'jhi-delivery-update',
  templateUrl: './delivery-update.component.html',
  styleUrls: ['./delivery-update.component.scss'],
})
export class DeliveryUpdateComponent {

  

  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;
  __data?: IData; 

    
  editForm = this.fb.group({
    id: [],
    subject:['', [Validators.required]],
    body:['', [Validators.required]],
    mDeptList: [],
    cDeptList: [],
    fileList: this.fb.array([]),

  });

  name = 'Progress Bar';
  isInfo = true;
  isReceiver = false;
  isAttachment = false;
  isSuccess = false;
  public counts = ["Info","Receiver","Attachment","Success"];

  public status = "Info"

  constructor(
    protected fb: FormBuilder
  ) {}
  

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

}
