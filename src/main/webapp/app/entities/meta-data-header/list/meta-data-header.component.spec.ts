import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MetaDataHeaderService } from '../service/meta-data-header.service';

import { MetaDataHeaderComponent } from './meta-data-header.component';

describe('Component Tests', () => {
  describe('MetaDataHeader Management Component', () => {
    let comp: MetaDataHeaderComponent;
    let fixture: ComponentFixture<MetaDataHeaderComponent>;
    let service: MetaDataHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MetaDataHeaderComponent],
      })
        .overrideTemplate(MetaDataHeaderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MetaDataHeaderComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MetaDataHeaderService);

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
      expect(comp.metaDataHeaders?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
