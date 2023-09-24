import { Component, OnInit } from '@angular/core';
import { IDocumentAttachment, IDocumentDelivery, IDocumentReceiver } from '../delivery.model';
import { ActivatedRoute } from '@angular/router';
import { IDepartment } from 'app/entities/department/department.model';

@Component({
  selector: 'jhi-delivery-detail',
  templateUrl: './delivery-detail.component.html',
  styleUrls: ['./delivery-detail.component.scss'],
})
export class DeliveryDetailComponent implements OnInit {
  documentDelivery?: IDocumentDelivery;
  receiverList?: IDocumentReceiver[] = [];
  attachmentList?: IDocumentAttachment[] = [];
  docNo: string | undefined;
  subject: string | undefined;
  body: string | undefined;

  toDepartments?: IDepartment[] = [];
  ccDepartments?: IDepartment[] = [];
  modules = {};

  constructor(protected activatedRoute: ActivatedRoute) {
    this.modules = {
      'toolbar': [
        ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
        ['blockquote', 'code-block'],

         [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
         [{ 'font': [] }],
         [{ 'align': [] }],
      ]
    }  
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delivery }) => {
      this.documentDelivery = delivery?.documentDelivery;
      this.receiverList = delivery?.receiverList;
      this.attachmentList = delivery?.attachmentList;
    });

    this.getData();

  }

  previousState(): void {
    window.history.back();
  }
    
  getData(): void {
    const tolist = this.receiverList!.map(receiverDept => receiverDept.receiver);
    this.docNo = this.documentDelivery?.referenceNo;
    this.subject = this.documentDelivery?.subject;
    this.body = this.documentDelivery?.description;

      
    this.receiverList?.forEach((value, index) => {
      if(value.receiverType === 1){
        this.toDepartments?.push(value.receiver!) ;
      }else{
        this.ccDepartments?.push(value.receiver!) ;
      }
    });

  }
}

