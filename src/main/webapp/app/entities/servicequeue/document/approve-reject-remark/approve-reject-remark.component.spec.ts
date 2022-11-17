import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveRejectRemarkComponent } from './approve-reject-remark.component';

describe('ApproveRejectRemarkComponent', () => {
  let component: ApproveRejectRemarkComponent;
  let fixture: ComponentFixture<ApproveRejectRemarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApproveRejectRemarkComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveRejectRemarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
