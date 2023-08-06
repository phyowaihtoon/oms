import { TestBed } from '@angular/core/testing';

import { MeetingRoutingResolveService } from './meeting-routing-resolve.service';

describe('MeetingRoutingResolveService', () => {
  let service: MeetingRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MeetingRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
