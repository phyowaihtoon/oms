jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MetaDataHeaderService } from '../service/meta-data-header.service';
import { IMetaDataHeader, MetaDataHeader } from '../meta-data-header.model';

import { MetaDataHeaderUpdateComponent } from './meta-data-header-update.component';

describe('Component Tests', () => {
  describe('MetaDataHeader Management Update Component', () => {
    let comp: MetaDataHeaderUpdateComponent;
    let fixture: ComponentFixture<MetaDataHeaderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let metaDataHeaderService: MetaDataHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MetaDataHeaderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MetaDataHeaderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MetaDataHeaderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      metaDataHeaderService = TestBed.inject(MetaDataHeaderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const metaDataHeader: IMetaDataHeader = { id: 456 };

        activatedRoute.data = of({ metaDataHeader });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(metaDataHeader));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaDataHeader = { id: 123 };
        spyOn(metaDataHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaDataHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: metaDataHeader }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(metaDataHeaderService.update).toHaveBeenCalledWith(metaDataHeader);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaDataHeader = new MetaDataHeader();
        spyOn(metaDataHeaderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaDataHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: metaDataHeader }));
        saveSubject.complete();

        // THEN
        expect(metaDataHeaderService.create).toHaveBeenCalledWith(metaDataHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const metaDataHeader = { id: 123 };
        spyOn(metaDataHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ metaDataHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(metaDataHeaderService.update).toHaveBeenCalledWith(metaDataHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
