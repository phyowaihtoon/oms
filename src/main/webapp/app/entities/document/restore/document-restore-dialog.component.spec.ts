import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentRestoreDialogComponent } from './document-restore-dialog.component';

describe('DocumentRestoreDialogComponent', () => {
  let component: DocumentRestoreDialogComponent;
  let fixture: ComponentFixture<DocumentRestoreDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DocumentRestoreDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentRestoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
