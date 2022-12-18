import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-role.test-samples';

import { UserRoleFormService } from './user-role-form.service';

describe('UserRole Form Service', () => {
  let service: UserRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRoleFormService);
  });

  describe('Service methods', () => {
    describe('createUserRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
          })
        );
      });

      it('passing IUserRole should create a new form with FormGroup', () => {
        const formGroup = service.createUserRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
          })
        );
      });
    });

    describe('getUserRole', () => {
      it('should return NewUserRole for default UserRole initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserRoleFormGroup(sampleWithNewData);

        const userRole = service.getUserRole(formGroup) as any;

        expect(userRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserRole for empty UserRole initial value', () => {
        const formGroup = service.createUserRoleFormGroup();

        const userRole = service.getUserRole(formGroup) as any;

        expect(userRole).toMatchObject({});
      });

      it('should return IUserRole', () => {
        const formGroup = service.createUserRoleFormGroup(sampleWithRequiredData);

        const userRole = service.getUserRole(formGroup) as any;

        expect(userRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserRole should not enable id FormControl', () => {
        const formGroup = service.createUserRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserRole should disable id FormControl', () => {
        const formGroup = service.createUserRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
