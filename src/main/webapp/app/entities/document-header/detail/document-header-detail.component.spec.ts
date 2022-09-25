import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentHeaderDetailComponent } from './document-header-detail.component';

describe('Component Tests', () => {
  describe('DocumentHeader Management Detail Component', () => {
    let comp: DocumentHeaderDetailComponent;
    let fixture: ComponentFixture<DocumentHeaderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentHeaderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentHeader: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentHeaderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentHeaderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentHeader on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentHeader).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
