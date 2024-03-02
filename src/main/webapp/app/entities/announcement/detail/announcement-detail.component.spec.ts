import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnnouncementDetailComponent } from './announcement-detail.component';

describe('Component Tests', () => {
  describe('Announcement Management Detail Component', () => {
    let comp: AnnouncementDetailComponent;
    let fixture: ComponentFixture<AnnouncementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AnnouncementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ announcement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AnnouncementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnnouncementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load announcement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.announcement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
