import { FormBuilder, Validators } from '@angular/forms';
import { DashboardService } from './../services/dashboard-service';
import { Component, Input, OnInit, AfterViewInit } from '@angular/core';
import { SchowChartService } from './showchart.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpResponse } from '@angular/common/http';
import { IPieHeaderDataDto } from 'app/services/pieheaderdata.model';
import { IMetaDataHeader } from 'app/entities/metadata/metadata.model';
import { ActivatedRoute } from '@angular/router';
import { IUserAuthority } from 'app/login/userauthority.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IInputParam, InputParam } from 'app/services/input-param.model';
import { UserAuthorityService } from 'app/login/userauthority.service';

@Component({
  selector: 'jhi-showchart',
  templateUrl: './showchart.component.html',
  styleUrls: ['./showchart.scss'],
})
export class ShowChartComponent implements AfterViewInit, OnInit {
  @Input() template: any;
  totalCount = 0;

  _metaDataHdrList?: IMetaDataHeader[] | null;
  _userAuthority?: IUserAuthority | null;

  editForm = this.fb.group({
    metaDataHdrID: [0, [Validators.required, Validators.pattern('^[1-9]*$')]],
    fromDate: [],
    toDate: [],
  });
  dateJoined: any;
  constructor(
    protected fb: FormBuilder,
    private dashboard: DashboardService,
    private showChart: SchowChartService,
    protected userAuthorityService: UserAuthorityService,
    protected modalService: NgbModal,
    protected domSanitizer: DomSanitizer,
    protected activatedRoute: ActivatedRoute,
    protected loadSetupService: LoadSetupService
  ) {}

  ngAfterViewInit(): void {
    this.showData();
  }

  ngOnInit(): void {
    this._userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this.loadAllSetup();
  }

  today(): any {
    const todayDate = new Date();
    return {
      year: todayDate.getFullYear(),
      month: todayDate.getMonth() + 1,
      day: todayDate.getDate(),
    };
  }

  getTotalRecordCount(): string {
    return 'Total - ' + this.totalCount.toString() + (this.totalCount > 0 ? ' Records' : ' Record');
  }

  loadAllSetup(): void {
    if (this._userAuthority) {
      this.loadSetupService.loadAllMetaDataHeaderByUserRole(this._userAuthority.roleID).subscribe(
        (res: HttpResponse<IMetaDataHeader[]>) => {
          if (res.body) {
            this._metaDataHdrList = res.body;
            if (this._metaDataHdrList.length > 0) {
              this.editForm.get(['metaDataHdrID'])?.patchValue(this._metaDataHdrList[0].id);
              this.onChangeDocumentTemplate();
            }
          }
        },
        error => {
          console.log('Loading MetaData Header Failed : ', error);
        }
      );
    }
  }

  trackMetaDataByID(index: number, item: IMetaDataHeader): number {
    return item.id!;
  }

  onChangeDocumentTemplate(): void {
    this.showData();
  }

  showData(): any {
    /*
    const innerEle = document.getElementById(this.template.cardId);
    if(innerEle !==null){
      innerEle.innerHTML = '' ;
    }
    */

    if (this.template.cardId === 'CARD002' || this.template.cardId === 'CARD003') {
      this.showChart.getAllSummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body));
        }
      });
    }

    if (this.template.cardId === 'CARD003') {
      this.showChart.getTodaySummaryData().subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body));
        }
      });
    }

    if (this.template.cardId === 'CARD004') {
      const inputParam = this.createParam();
      this.showChart.getDataByTemplate(inputParam).subscribe((res: HttpResponse<[]>) => {
        if (res.body) {
          const cols: any = [];
          // tslint:disable-next-line: typedef
          res.body.forEach(function (e: any) {
            // tslint:disable-next-line: typedef
            e.detail.forEach(function (d: any) {
              // tslint:disable-next-line: typedef
              if (
                cols.filter(function (t: any) {
                  return t === d.name;
                }).length === 0
              ) {
                cols.push(d.name);
              }
            });
          });
          this.dashboard.generateLineChart(this.template.cardId, this.prepareData(res.body, cols), cols, 'Count');
        }
      });
    }

    if (this.template.cardId === 'CARD005') {
      const inputParam = this.createParam();
      this.showChart.getTodaySummaryByTemplate(inputParam).subscribe((res: HttpResponse<IPieHeaderDataDto>) => {
        if (res.body) {
          this.dashboard.generatePieChart(this.template.cardId, this.preparePieData(res.body));
        }
      });
    }
  }

  prepareData(data: any, cols: any): any {
    const _tempObj: { name: any; data: any }[] = [];
    data.forEach((d: any) => {
      const count: any = [];
      cols.forEach((e: any) => {
        let value = 0;
        d.detail.forEach((f: any) => {
          if (e === f.name) {
            value = f.count;
          }
        });
        count.push(value);
      });
      _tempObj.push({ name: d.type, data: count });
    });
    return _tempObj;
  }
  createParam(): IInputParam {
    return {
      ...new InputParam(),
      templateId: this.editForm.get(['metaDataHdrID'])!.value,
      // fromDate: this.editForm.get('fromDate')!.value ? this.editForm.get('fromDate')!.value.format('DD-MM-YYYY') : '',
      // toDate: this.editForm.get('toDate')!.value ? this.editForm.get('toDate')!.value.format('DD-MM-YYYY') : '',
    };
  }

  preparePieData(req: any): any {
    this.totalCount = req.totalCount;
    const ret: any = [];
    req.data?.forEach((d: any) => {
      ret.push({ name: d.name, y: d.data });
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
