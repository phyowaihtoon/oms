import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleMenuMapComponent } from './role-menu-map.component';

describe('RoleMenuMapComponent', () => {
  let component: RoleMenuMapComponent;
  let fixture: ComponentFixture<RoleMenuMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoleMenuMapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleMenuMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
