jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MetaDataService } from '../service/meta-data.service';
import { IMetaData, MetaData } from '../meta-data.model';

import { MetaDataUpdateComponent } from './meta-data-update.component';

describe('Component Tests', () => {
  describe('MetaData Management Update Component', () => {
    let comp: MetaDataUpdateComponent;
    let fixture: ComponentFixture<MetaDataUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let metaDataService: MetaDataService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MetaDataUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MetaDataUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MetaDataUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      metaDataService = TestBed.inject(MetaDataService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const metaData: IMetaData = { id: 456 };

        activatedRoute.data = of({ metaData });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(metaData));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaData = { id: 123 };
        spyOn(metaDataService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: metaData }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(metaDataService.update).toHaveBeenCalledWith(metaData);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaData = new MetaData();
        spyOn(metaDataService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: metaData }));
        saveSubject.complete();

        // THEN
        expect(metaDataService.create).toHaveBeenCalledWith(metaData);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaData = { id: 123 };
        spyOn(metaDataService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(metaDataService.update).toHaveBeenCalledWith(metaData);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
