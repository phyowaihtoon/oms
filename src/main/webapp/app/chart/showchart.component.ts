import { FormBuilder } from '@angular/forms';
import { DashboardService } from './../services/dashboard-service';
import { Component, Input, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SchowChartService } from './showchart.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-showchart',
  templateUrl: './showchart.component.html',
  styleUrls: ['./showchart.scss'],
})
export class ShowChartComponent implements AfterViewInit {
  @Input() template: any;

  constructor(private dashboard: DashboardService, protected modalService: NgbModal, protected domSanitizer: DomSanitizer) {}

  ngAfterViewInit(): void {
    this.showData();
  }

  showData(): any {
    /*
    const innerEle = document.getElementById(this.template.cardId);
    if(innerEle !==null){
      innerEle.innerHTML = '' ;
    }
    */

    if (this.template.cardId === 'CARD002') {
      this.dashboard.generatePieChart(this.template.cardId, this.preparePieData());
    }
  }

  preparePieData(): any {
    return [
      {
        name: 'Documents',
        colorByPoint: true,
        data: [
          {
            name: 'New - 10 Records',
            y: 65.5,
            sliced: true,
            selected: true,
          },
          {
            name: 'Sent for Approval - 15 Records',
            y: 11.3,
          },
          {
            name: 'Sent for Amendment - 12 Records',
            y: 2.5,
          },
          {
            name: 'Approved - 2 Records',
            y: 4.6,
          },
          {
            name: 'Rejected - 1 Record',
            y: 7.2,
          },
          {
            name: 'Canceled - 5 Records',
            y: 8.9,
          },
        ],
      },
    ];
  }

  /*
  prepareData(data:any, cols:any):any {
    const _tempObj :any = [];
    data.forEach((d:any) => {
      const amount :any = [];
      cols.forEach((e:any) => {
        let value = 0;
        d.detail.forEach((f:any) => {
          if (e === f.name) {
            value = f.amount;
          }
        });
        amount.push(value);
      });
      _tempObj.push({ name: d.type, data: amount });
    });
    return _tempObj;
  }
  */
}
