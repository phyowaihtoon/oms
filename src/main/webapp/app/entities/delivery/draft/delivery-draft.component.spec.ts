import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryDraftComponent } from './delivery-draft.component';

describe('DeliveryDraftComponent', () => {
  let component: DeliveryDraftComponent;
  let fixture: ComponentFixture<DeliveryDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeliveryDraftComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
