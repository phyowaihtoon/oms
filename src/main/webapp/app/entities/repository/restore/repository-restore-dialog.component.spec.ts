import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryRestoreDialogComponent } from './repository-restore-dialog.component';

describe('RepositoryRestoreDialogComponent', () => {
  let component: RepositoryRestoreDialogComponent;
  let fixture: ComponentFixture<RepositoryRestoreDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepositoryRestoreDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryRestoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
