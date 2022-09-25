import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RepositoryDetailComponent } from './repository-detail.component';

describe('Component Tests', () => {
  describe('Repository Management Detail Component', () => {
    let comp: RepositoryDetailComponent;
    let fixture: ComponentFixture<RepositoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RepositoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ repository: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RepositoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RepositoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load repository on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.repository).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
