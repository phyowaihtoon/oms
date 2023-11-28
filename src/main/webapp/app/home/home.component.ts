import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbCalendar, NgbDate, NgbDateStruct, NgbDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { IDocumentDelivery } from 'app/entities/delivery/delivery.model';
import { DeliveryService } from 'app/entities/delivery/service/delivery.service';
import { IMeetingDelivery } from 'app/entities/meeting/meeting.model';
import { MeetingService } from 'app/entities/meeting/service/meeting.service';
import { SearchCriteria } from 'app/entities/util/criteria.model';
import { LoadSetupService } from 'app/entities/util/load-setup.service';
import { IDraftSummary } from 'app/entities/util/setup.model';
import { IUserAuthority } from 'app/login/userauthority.model';
import { UserAuthorityService } from 'app/login/userauthority.service';
import { Subscription } from 'rxjs';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('dp') datepicker?: NgbDatepicker;

  account: Account | null = null;
  authSubscription?: Subscription;
  _userAuthority?: IUserAuthority | null;

  itemsPerPage = ITEMS_PER_PAGE;

  totalItems_1 = 10;
  page_1?: number;
  ngbPaginationPage_1 = 1;

  totalItems_2 = 10;
  page_2?: number;
  ngbPaginationPage_2 = 1;

  _departmentName: string | undefined = '';
  _lDate: NgbDate = this.calendar.getToday();
  _lStatus = 0;
  _draftSummary: IDraftSummary | undefined;
  _meetingList: IMeetingDelivery[] = [];

  _dashboardData1: IDocumentDelivery[] = [];
  _dashboardData2: IDocumentDelivery[] = [];

  constructor(
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected userAuthorityService: UserAuthorityService,
    private calendar: NgbCalendar,
    private loadSetupService: LoadSetupService,
    private meetingService: MeetingService,
    private deliveryService: DeliveryService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    /*  if (this.isAuthenticated()) {
      this._userAuthority = this.userAuthorityService.retrieveUserAuthority();
      this.templates = this._userAuthority?.dashboardTemplates;
    } */
    const userAuthority = this.userAuthorityService.retrieveUserAuthority();
    this._departmentName = userAuthority?.department?.departmentName;
    this.getDraftSummary();
    this.getMeetingList();
    this.loadDashboard1(1);
    this.loadDashboard2(1);
  }

  ngAfterViewInit(): void {
    this.makeTodayDateSelected();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  format(date: NgbDateStruct | null): string {
    if (date) {
      console.log('This is Date');
      const something = dayjs(new Date(Date.UTC(date.year, date.month - 1, date.day)));
      return something.format('DD-MM-YYYY');
    }
    return ' ';
  }

  onDateSelected(e: any): void {
    this._lDate = e;
    this._dashboardData1 = [];
    this._dashboardData2 = [];
    this.loadDashboard1(1);
    this.loadDashboard2(1);
  }

  onStatusChange(e: any): void {
    this._lStatus = e.target.value;
    this._dashboardData1 = [];
    this.loadDashboard1(1);
  }

  onRefresh(): void {
    this.makeTodayDateSelected();

    this._lDate = this.calendar.getToday();
    this.getDraftSummary();
    this.getMeetingList();
    this._dashboardData1 = [];
    this._dashboardData2 = [];
    this.loadDashboard1(1);
    this.loadDashboard2(1);
  }

  getDraftSummary(): void {
    this.loadSetupService.loadDraftSummary().subscribe(
      (res: HttpResponse<IDraftSummary>) => {
        if (res.body) {
          this._draftSummary = res.body;
        }
      },
      error => {
        console.log('Loading Draft Summary Failed : ', error);
      }
    );
  }

  getMeetingList(): void {
    this.meetingService.getScheduledMeetingList().subscribe(
      (res: HttpResponse<IMeetingDelivery[]>) => {
        if (res.body) {
          this._meetingList = res.body;
        }
      },
      error => {
        console.log('Loading Meeting List Failed : ', error);
      }
    );
  }

  loadDashboard1(page?: number): void {
    const pageToLoad: number = page ?? this.page_2 ?? 1;
    const _dateOn: string = String(this._lDate.day) + '-' + String(this._lDate.month) + '-' + String(this._lDate.year);

    const Criteria = { ...new SearchCriteria(), requestFrom: 1, status: this._lStatus, dateOn: _dateOn };

    const requestParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      criteria: JSON.stringify(Criteria),
    };

    this.deliveryService.findAllReceived(requestParams).subscribe(
      (res: HttpResponse<IDocumentDelivery[]>) => {
        if (!res.ok) {
          console.log('Error Message :', res.headers.get('message'));
        }
        this.onSuccess1(res.body, res.headers, pageToLoad);
      },
      error => {
        console.log('This Error Message :', error.headers.get('message'));
        this.onError1();
      }
    );
  }

  onSuccess1(data: IDocumentDelivery[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems_1 = Number(headers.get('X-Total-Count'));
    this.page_1 = page;
    this._dashboardData1 = data ?? [];
    this.ngbPaginationPage_1 = this.page_1;
  }

  onError1(): void {
    this.ngbPaginationPage_1 = this.page_1 ?? 1;
  }

  loadDashboard2(page?: number): void {
    const pageToLoad: number = page ?? this.page_2 ?? 1;
    const _dateOn: string = String(this._lDate.day) + '-' + String(this._lDate.month) + '-' + String(this._lDate.year);
    const Criteria = { ...new SearchCriteria(), requestFrom: 1, dateOn: _dateOn };

    const requestParams = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      criteria: JSON.stringify(Criteria),
    };

    this.deliveryService.findAllSent(requestParams).subscribe(
      (res: HttpResponse<IDocumentDelivery[]>) => {
        if (!res.ok) {
          console.log('Error Message :', res.headers.get('message'));
        }
        this.onSuccess2(res.body, res.headers, pageToLoad);
      },
      error => {
        console.log('This Error Message :', error.headers.get('message'));
        this.onError2();
      }
    );
  }

  onSuccess2(data: IDocumentDelivery[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems_2 = Number(headers.get('X-Total-Count'));
    this.page_2 = page;
    this._dashboardData2 = data ?? [];
    this.ngbPaginationPage_2 = this.page_2;
  }

  onError2(): void {
    this.ngbPaginationPage_2 = this.page_2 ?? 1;
  }

  private makeTodayDateSelected(): void {
    const today = this.calendar.getToday();
    const dateToFocus: NgbDateStruct = { year: today.year, month: today.month, day: today.day };
    this.datepicker?.focusDate(dateToFocus);
    this.datepicker?.focusSelect();
  }
}
