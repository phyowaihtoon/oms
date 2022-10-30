import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentQueueComponent } from './document-queue.component';

describe('DocumentQueueComponent', () => {
  let component: DocumentQueueComponent;
  let fixture: ComponentFixture<DocumentQueueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentQueueComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentQueueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
