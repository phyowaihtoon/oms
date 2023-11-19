import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentDeleteDialogComponent } from './document-delete-dialog.component';

describe('DocumentDeleteDialogComponent', () => {
  let component: DocumentDeleteDialogComponent;
  let fixture: ComponentFixture<DocumentDeleteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentDeleteDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
