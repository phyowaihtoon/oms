import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetadataRestoreDialogComponent } from './metadata-restore-dialog.component';

describe('MetadataRestoreDialogComponent', () => {
  let component: MetadataRestoreDialogComponent;
  let fixture: ComponentFixture<MetadataRestoreDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MetadataRestoreDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetadataRestoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
