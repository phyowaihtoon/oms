import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingSentComponent } from './meeting-sent.component';

describe('MeetingSentComponent', () => {
  let component: MeetingSentComponent;
  let fixture: ComponentFixture<MeetingSentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeetingSentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetingSentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
