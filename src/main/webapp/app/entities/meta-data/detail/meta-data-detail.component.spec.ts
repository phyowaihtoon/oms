import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MetaDataDetailComponent } from './meta-data-detail.component';

describe('Component Tests', () => {
  describe('MetaData Management Detail Component', () => {
    let comp: MetaDataDetailComponent;
    let fixture: ComponentFixture<MetaDataDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MetaDataDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ metaData: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MetaDataDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MetaDataDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load metaData on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.metaData).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
