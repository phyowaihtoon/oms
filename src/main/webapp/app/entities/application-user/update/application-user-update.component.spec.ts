jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicationUserService } from '../service/application-user.service';
import { IApplicationUser, ApplicationUser } from '../application-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { ApplicationUserUpdateComponent } from './application-user-update.component';

describe('Component Tests', () => {
  describe('ApplicationUser Management Update Component', () => {
    let comp: ApplicationUserUpdateComponent;
    let fixture: ComponentFixture<ApplicationUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let applicationUserService: ApplicationUserService;
    let userService: UserService;
    let departmentService: DepartmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplicationUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApplicationUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      applicationUserService = TestBed.inject(ApplicationUserService);
      userService = TestBed.inject(UserService);
      departmentService = TestBed.inject(DepartmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const applicationUser: IApplicationUser = { id: 456 };
        const user: IUser = { id: 85859 };
        applicationUser.user = user;

        const userCollection: IUser[] = [{ id: 59207 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Department query and add missing value', () => {
        const applicationUser: IApplicationUser = { id: 456 };
        const department: IDepartment = { id: 23015 };
        applicationUser.department = department;

        const departmentCollection: IDepartment[] = [{ id: 43580 }];
        spyOn(departmentService, 'query').and.returnValue(of(new HttpResponse({ body: departmentCollection })));
        const additionalDepartments = [department];
        const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
        spyOn(departmentService, 'addDepartmentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        expect(departmentService.query).toHaveBeenCalled();
        expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
        expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const applicationUser: IApplicationUser = { id: 456 };
        const user: IUser = { id: 84001 };
        applicationUser.user = user;
        const department: IDepartment = { id: 15534 };
        applicationUser.department = department;

        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(applicationUser));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.departmentsSharedCollection).toContain(department);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicationUser = { id: 123 };
        spyOn(applicationUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicationUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(applicationUserService.update).toHaveBeenCalledWith(applicationUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicationUser = new ApplicationUser();
        spyOn(applicationUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicationUser }));
        saveSubject.complete();

        // THEN
        expect(applicationUserService.create).toHaveBeenCalledWith(applicationUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicationUser = { id: 123 };
        spyOn(applicationUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicationUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(applicationUserService.update).toHaveBeenCalledWith(applicationUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDepartmentById', () => {
        it('Should return tracked Department primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
