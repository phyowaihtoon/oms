jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserRoleService } from '../service/user-role.service';
import { IUserRole, UserRole } from '../user-role.model';

import { UserRoleUpdateComponent } from './user-role-update.component';

describe('Component Tests', () => {
  describe('UserRole Management Update Component', () => {
    let comp: UserRoleUpdateComponent;
    let fixture: ComponentFixture<UserRoleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userRoleService: UserRoleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserRoleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserRoleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserRoleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userRoleService = TestBed.inject(UserRoleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const userRole: IUserRole = { id: 456 };

        activatedRoute.data = of({ userRole });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userRole));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userRole = { id: 123 };
        spyOn(userRoleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userRole }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userRoleService.update).toHaveBeenCalledWith(userRole);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userRole = new UserRole();
        spyOn(userRoleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userRole }));
        saveSubject.complete();

        // THEN
        expect(userRoleService.create).toHaveBeenCalledWith(userRole);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const userRole = { id: 123 };
        spyOn(userRoleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ userRole });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userRoleService.update).toHaveBeenCalledWith(userRole);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
