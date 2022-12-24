import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryTrashbinComponent } from './repository-trashbin.component';

describe('RepositoryTrashbinComponent', () => {
  let component: RepositoryTrashbinComponent;
  let fixture: ComponentFixture<RepositoryTrashbinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepositoryTrashbinComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryTrashbinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
