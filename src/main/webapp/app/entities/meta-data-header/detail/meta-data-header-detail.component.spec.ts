import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MetaDataHeaderDetailComponent } from './meta-data-header-detail.component';

describe('Component Tests', () => {
  describe('MetaDataHeader Management Detail Component', () => {
    let comp: MetaDataHeaderDetailComponent;
    let fixture: ComponentFixture<MetaDataHeaderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MetaDataHeaderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ metaDataHeader: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MetaDataHeaderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MetaDataHeaderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load metaDataHeader on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.metaDataHeader).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
