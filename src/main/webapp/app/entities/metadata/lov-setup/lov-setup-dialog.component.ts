import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators, FormGroup, FormArray } from '@angular/forms';

@Component({
  selector: 'jhi-lov-setup-dialog',
  templateUrl: './lov-setup-dialog.component.html',
  styleUrls: ['./lov-setup-dialog.component.scss'],
})
export class LovSetupDialogComponent implements OnInit {
  lovStr = '';
  fieldName = '';

  editForm = this.fb.group({
    fieldList: this.fb.array([]),
  });

  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  constructor(public activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {
    2;
    this.addField();
    if (this.lovStr.length > 0) {
      this.removeAllField();
      this.updateForm();
    }
  }

  updateForm(): void {
    this.editForm.patchValue({
      fieldList: this.updateLovValue(),
    });
  }

  updateLovValue(): void {
    let index = 0;
    const lovList = this.lovStr.split('|');
    lovList.forEach(data => {
      this.addField();
      this.fieldList().controls[index].get(['lovValue'])!.setValue(data);
      index = index + 1;
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  add(): void {
    let lov = '';
    this.fieldList().controls.forEach(data => {
      if (lov === '') {
        lov = String(data.get(['lovValue'])!.value);
      } else {
        lov = lov + '|' + String(data.get(['lovValue'])!.value);
      }
    });

    this.passEntry.emit(lov);
    this.activeModal.dismiss();
  }

  fieldList(): FormArray {
    return this.editForm.get('fieldList') as FormArray;
  }

  newField(): FormGroup {
    return this.fb.group({
      lovValue: ['', [Validators.required, Validators.maxLength(50)]],
    });
  }

  addField(): void {
    this.fieldList().push(this.newField());
  }

  removeField(i: number): void {
    if (this.fieldList().length > 1) {
      this.fieldList().removeAt(i);
    }
  }

  removeAllField(): void {
    this.fieldList().clear();
  }
}
