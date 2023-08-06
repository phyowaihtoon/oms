import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingDraftComponent } from './meeting-draft.component';

describe('MeetingDraftComponent', () => {
  let component: MeetingDraftComponent;
  let fixture: ComponentFixture<MeetingDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeetingDraftComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetingDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
