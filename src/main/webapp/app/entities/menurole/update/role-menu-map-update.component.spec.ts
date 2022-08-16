import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleMenuMapUpdateComponent } from './role-menu-map-update.component';

describe('RoleMenuMapUpdateComponent', () => {
  let component: RoleMenuMapUpdateComponent;
  let fixture: ComponentFixture<RoleMenuMapUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RoleMenuMapUpdateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleMenuMapUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
