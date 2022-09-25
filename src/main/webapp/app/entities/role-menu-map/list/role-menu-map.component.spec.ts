import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RoleMenuMapService } from '../service/role-menu-map.service';

import { RoleMenuMapComponent } from './role-menu-map.component';

describe('Component Tests', () => {
  describe('RoleMenuMap Management Component', () => {
    let comp: RoleMenuMapComponent;
    let fixture: ComponentFixture<RoleMenuMapComponent>;
    let service: RoleMenuMapService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoleMenuMapComponent],
      })
        .overrideTemplate(RoleMenuMapComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoleMenuMapComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RoleMenuMapService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.roleMenuMaps?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
