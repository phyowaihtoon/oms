import { FormBuilder } from '@angular/forms';
import { DashboardService } from './../services/dashboard-service';
import { Component, Input, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SchowChartService } from './showchart.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpResponse } from '@angular/common/http';
import { IPieHeaderDataDto } from 'app/services/pieheaderdata.model';

@Component({
  selector: 'jhi-showchart',
  templateUrl: './showchart.component.html',
  styleUrls: ['./showchart.scss'],
})
export class ShowChartComponent implements AfterViewInit {
  @Input() template: any;
  totalCount: any;
  constructor(
    private dashboard: DashboardService,
    private showChart: SchowChartService,
    protected modalService: NgbModal,
    protected domSanitizer: DomSanitizer
  ) {}

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
      this.showChart.getAllSummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto[]>) => {
        console.log('body=>', res.body);
        if (res.body) {
          console.log('writing data');
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body));
        }
      });
    }

    if (this.template.cardId === 'CARD003') {
      this.showChart.getTodaySummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto[]>) => {
        console.log('body=>', res.body);
        if (res.body) {
          console.log('writing data');
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body));
        }
      });
    }
  }

  preparePieData(req: any): any {
    this.totalCount = req.totalCount;
    const ret: any = [];
    req.data?.forEach((d: any) => {
      console.log(d.name);
      ret.push({ name: d.name, y: d.data });
      //  ret.push({ name: data.name, y: data.amount });
    });

    return [
      {
        name: 'Documents',
        colorByPoint: true,
        data: ret,
      },
    ];
  }

  preparePieDataOld(): any {
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
