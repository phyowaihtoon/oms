import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryReceivedComponent } from './delivery-received.component';

describe('DeliveryReceivedComponent', () => {
  let component: DeliveryReceivedComponent;
  let fixture: ComponentFixture<DeliveryReceivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeliveryReceivedComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryReceivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
