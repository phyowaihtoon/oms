import { Component, OnInit } from '@angular/core';
import { IDocumentAttachment, IDocumentDelivery, IDocumentReceiver } from '../delivery.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-delivery-detail',
  templateUrl: './delivery-detail.component.html',
  styleUrls: ['./delivery-detail.component.scss'],
})
export class DeliveryDetailComponent implements OnInit {
  documentDelivery?: IDocumentDelivery;
  receiverList?: IDocumentReceiver[] = [];
  attachmentList?: IDocumentAttachment[] = [];

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delivery }) => {
      this.documentDelivery = delivery?.documentDelivery;
      this.receiverList = delivery?.receiverList;
      this.attachmentList = delivery?.attachmentList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
