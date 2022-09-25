import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleMenuMapDetailComponent } from './role-menu-map-detail.component';

describe('Component Tests', () => {
  describe('RoleMenuMap Management Detail Component', () => {
    let comp: RoleMenuMapDetailComponent;
    let fixture: ComponentFixture<RoleMenuMapDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RoleMenuMapDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ roleMenuMap: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RoleMenuMapDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoleMenuMapDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load roleMenuMap on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.roleMenuMap).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
