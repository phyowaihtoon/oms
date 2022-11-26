import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DocumentService } from 'app/entities/document/service/document.service';

@Component({
  selector: 'jhi-approve-reject-remark',
  templateUrl: './approve-reject-remark.component.html',
  styleUrls: ['./approve-reject-remark.component.scss'],
})
export class ApproveRejectRemarkComponent implements OnInit {
  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  editForm = this.fb.group({
    reason: ['', [Validators.required]],
  });

  constructor(public activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {
    console.log('OnInit');
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirm(): void {
    let reason = '';
    reason = this.editForm.get(['reason'])!.value;
    this.passEntry.emit(reason);
    this.activeModal.dismiss();
  }
}
