import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { IDepartment } from 'app/entities/department/department.model';

@Component({
  selector: 'jhi-meeting-update',
  templateUrl: './meeting-update.component.html',
  styleUrls: ['./meeting-update.component.scss'],
})
export class MeetingUpdateComponent implements OnInit {
  @ViewChild('inputFileElement') myInputVariable: ElementRef | undefined;

  editForm = this.fb.group({
    id: [],
    subject: ['', [Validators.required]],
    body: ['', [Validators.required]],
    mDeptList: [],
    cDeptList: [],
    fileList: this.fb.array([]),
  });

  isInfo = true;
  isReceiver = false;
  isAttachment = false;
  isDelivery = false;
  public progressItems = [
    { step: 1, title: 'Info' },
    { step: 2, title: 'Receiver' },
    { step: 3, title: 'Attachment' },
    { step: 4, title: 'Delivery' },
  ];

  public progressStep = 1;

  toLabel = 'To:';
  ccLabel = 'Cc:';
  toDepartments?: IDepartment[] = [];
  ccDepartments?: IDepartment[] = [];

  constructor(protected fb: FormBuilder, protected translateService: TranslateService) {}

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

  goToStep1(): void {
    this.progressStep = 1;
    this.isInfo = true;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isDelivery = false;
  }

  goToStep2(): void {
    this.progressStep = 2;
    this.isInfo = false;
    this.isReceiver = true;
    this.isAttachment = false;
    this.isDelivery = false;
  }

  goToStep3(): void {
    this.progressStep = 3;
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = true;
    this.isDelivery = false;
  }

  goToStep4(): void {
    this.progressStep = 4;
    this.isInfo = false;
    this.isReceiver = false;
    this.isAttachment = false;
    this.isDelivery = true;
  }

  submit(): void {
    console.log('##### Save #####');
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
    }
  }

  onToDepartmentChange(event: any): void {
    this.toDepartments = event;
    console.log(this.toDepartments);
  }

  onCcDepartmentChange(event: any): void {
    this.ccDepartments = event;
    console.log(this.ccDepartments);
  }
}
