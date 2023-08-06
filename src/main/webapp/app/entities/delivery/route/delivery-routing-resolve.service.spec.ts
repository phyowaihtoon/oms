import { TestBed } from '@angular/core/testing';

import { DeliveryRoutingResolveService } from './delivery-routing-resolve.service';

describe('DeliveryRoutingResolveService', () => {
  let service: DeliveryRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
