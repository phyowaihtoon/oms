jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentHeaderService } from '../service/document-header.service';
import { IDocumentHeader, DocumentHeader } from '../document-header.model';

import { DocumentHeaderUpdateComponent } from './document-header-update.component';

describe('Component Tests', () => {
  describe('DocumentHeader Management Update Component', () => {
    let comp: DocumentHeaderUpdateComponent;
    let fixture: ComponentFixture<DocumentHeaderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentHeaderService: DocumentHeaderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentHeaderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentHeaderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentHeaderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentHeaderService = TestBed.inject(DocumentHeaderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const documentHeader: IDocumentHeader = { id: 456 };

        activatedRoute.data = of({ documentHeader });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(documentHeader));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const documentHeader = { id: 123 };
        spyOn(documentHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentHeader }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentHeaderService.update).toHaveBeenCalledWith(documentHeader);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const documentHeader = new DocumentHeader();
        spyOn(documentHeaderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: documentHeader }));
        saveSubject.complete();

        // THEN
        expect(documentHeaderService.create).toHaveBeenCalledWith(documentHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const documentHeader = { id: 123 };
        spyOn(documentHeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ documentHeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentHeaderService.update).toHaveBeenCalledWith(documentHeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
