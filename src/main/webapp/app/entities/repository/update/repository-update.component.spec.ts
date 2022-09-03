import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryUpdateComponent } from './repository-update.component';

describe('RepositoryUpdateComponent', () => {
  let component: RepositoryUpdateComponent;
  let fixture: ComponentFixture<RepositoryUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepositoryUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
