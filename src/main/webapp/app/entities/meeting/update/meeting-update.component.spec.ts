import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingUpdateComponent } from './meeting-update.component';

describe('MeetingUpdateComponent', () => {
  let component: MeetingUpdateComponent;
  let fixture: ComponentFixture<MeetingUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeetingUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetingUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
