jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnnouncementService } from '../service/announcement.service';
import { IAnnouncement, Announcement } from '../announcement.model';

import { AnnouncementUpdateComponent } from './announcement-update.component';

describe('Component Tests', () => {
  describe('Announcement Management Update Component', () => {
    let comp: AnnouncementUpdateComponent;
    let fixture: ComponentFixture<AnnouncementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let announcementService: AnnouncementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnouncementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AnnouncementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnouncementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      announcementService = TestBed.inject(AnnouncementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const announcement: IAnnouncement = { id: 456 };

        activatedRoute.data = of({ announcement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(announcement));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const announcement = { id: 123 };
        spyOn(announcementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ announcement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: announcement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(announcementService.update).toHaveBeenCalledWith(announcement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const announcement = new Announcement();
        spyOn(announcementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ announcement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: announcement }));
        saveSubject.complete();

        // THEN
        expect(announcementService.create).toHaveBeenCalledWith(announcement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const announcement = { id: 123 };
        spyOn(announcementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ announcement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(announcementService.update).toHaveBeenCalledWith(announcement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
