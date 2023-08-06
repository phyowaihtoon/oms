import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingReceivedComponent } from './meeting-received.component';

describe('MeetingReceivedComponent', () => {
  let component: MeetingReceivedComponent;
  let fixture: ComponentFixture<MeetingReceivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeetingReceivedComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetingReceivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
