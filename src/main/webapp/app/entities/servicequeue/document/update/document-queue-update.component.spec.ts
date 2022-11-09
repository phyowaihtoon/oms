import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentQueueUpdateComponent } from './document-queue-update.component';

describe('DocumentQueueUpdateComponent', () => {
  let component: DocumentQueueUpdateComponent;
  let fixture: ComponentFixture<DocumentQueueUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentQueueUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentQueueUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
