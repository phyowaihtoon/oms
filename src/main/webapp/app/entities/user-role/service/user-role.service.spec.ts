import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserRole, UserRole } from '../user-role.model';

import { UserRoleService } from './user-role.service';

describe('Service Tests', () => {
  describe('UserRole Service', () => {
    let service: UserRoleService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserRole;
    let expectedResult: IUserRole | IUserRole[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UserRoleService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        roleName: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a UserRole', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UserRole()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserRole', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roleName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UserRole', () => {
        const patchObject = Object.assign({}, new UserRole());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UserRole', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roleName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a UserRole', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUserRoleToCollectionIfMissing', () => {
        it('should add a UserRole to an empty array', () => {
          const userRole: IUserRole = { id: 123 };
          expectedResult = service.addUserRoleToCollectionIfMissing([], userRole);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userRole);
        });

        it('should not add a UserRole to an array that contains it', () => {
          const userRole: IUserRole = { id: 123 };
          const userRoleCollection: IUserRole[] = [
            {
              ...userRole,
            },
            { id: 456 },
          ];
          expectedResult = service.addUserRoleToCollectionIfMissing(userRoleCollection, userRole);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UserRole to an array that doesn't contain it", () => {
          const userRole: IUserRole = { id: 123 };
          const userRoleCollection: IUserRole[] = [{ id: 456 }];
          expectedResult = service.addUserRoleToCollectionIfMissing(userRoleCollection, userRole);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userRole);
        });

        it('should add only unique UserRole to an array', () => {
          const userRoleArray: IUserRole[] = [{ id: 123 }, { id: 456 }, { id: 9720 }];
          const userRoleCollection: IUserRole[] = [{ id: 123 }];
          expectedResult = service.addUserRoleToCollectionIfMissing(userRoleCollection, ...userRoleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const userRole: IUserRole = { id: 123 };
          const userRole2: IUserRole = { id: 456 };
          expectedResult = service.addUserRoleToCollectionIfMissing([], userRole, userRole2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userRole);
          expect(expectedResult).toContain(userRole2);
        });

        it('should accept null and undefined values', () => {
          const userRole: IUserRole = { id: 123 };
          expectedResult = service.addUserRoleToCollectionIfMissing([], null, userRole, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userRole);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
