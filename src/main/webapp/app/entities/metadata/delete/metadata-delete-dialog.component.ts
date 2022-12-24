import { HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IReplyMessage, ResponseCode } from 'app/entities/util/reply-message.model';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMetaDataHeader } from '../metadata.model';
import { MetaDataService } from '../service/metadata.service';

@Component({
  templateUrl: './metadata-delete-dialog.component.html',
})
export class MetaDataDeleteDialogComponent {
  metadata?: IMetaDataHeader;

  constructor(protected service: MetaDataService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subscribeToSaveResponse(this.service.delete(id));

    // this.service.delete(id).subscribe(() => {
    //   this.activeModal.close('deleted');
    // });
  }

  protected onSaveFinalize(): void {
    console.log(' ');
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
      if (replyMessage.code === ResponseCode.ERROR_E00) {
        const replyCode = replyMessage.code;
        const replyMsg = replyMessage.message;
        this.activeModal.close('not-deleted');
      } else {
        this.activeModal.close('deleted');
      }
    } else {
      this.onSaveError();
    }
  }

  protected onSaveError(): void {
    const replyCode = ResponseCode.RESPONSE_FAILED_CODE;
    const replyMsg = 'Error occured while connecting to server. Please, check network connection with your server.';
    // this.showAlertMessage(replyCode, replyMsg);
  }
}
