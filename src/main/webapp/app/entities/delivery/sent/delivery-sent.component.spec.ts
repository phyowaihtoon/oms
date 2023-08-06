import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliverySentComponent } from './delivery-sent.component';

describe('DeliverySentComponent', () => {
  let component: DeliverySentComponent;
  let fixture: ComponentFixture<DeliverySentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeliverySentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliverySentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
