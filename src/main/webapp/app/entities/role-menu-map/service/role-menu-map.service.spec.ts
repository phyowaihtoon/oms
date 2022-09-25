import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoleMenuMap, RoleMenuMap } from '../role-menu-map.model';

import { RoleMenuMapService } from './role-menu-map.service';

describe('Service Tests', () => {
  describe('RoleMenuMap Service', () => {
    let service: RoleMenuMapService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoleMenuMap;
    let expectedResult: IRoleMenuMap | IRoleMenuMap[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RoleMenuMapService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        roleId: 'AAAAAAA',
        menuId: 0,
        allowed: 'AAAAAAA',
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

      it('should create a RoleMenuMap', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RoleMenuMap()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RoleMenuMap', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roleId: 'BBBBBB',
            menuId: 1,
            allowed: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RoleMenuMap', () => {
        const patchObject = Object.assign({}, new RoleMenuMap());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RoleMenuMap', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roleId: 'BBBBBB',
            menuId: 1,
            allowed: 'BBBBBB',
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

      it('should delete a RoleMenuMap', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRoleMenuMapToCollectionIfMissing', () => {
        it('should add a RoleMenuMap to an empty array', () => {
          const roleMenuMap: IRoleMenuMap = { id: 123 };
          expectedResult = service.addRoleMenuMapToCollectionIfMissing([], roleMenuMap);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roleMenuMap);
        });

        it('should not add a RoleMenuMap to an array that contains it', () => {
          const roleMenuMap: IRoleMenuMap = { id: 123 };
          const roleMenuMapCollection: IRoleMenuMap[] = [
            {
              ...roleMenuMap,
            },
            { id: 456 },
          ];
          expectedResult = service.addRoleMenuMapToCollectionIfMissing(roleMenuMapCollection, roleMenuMap);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RoleMenuMap to an array that doesn't contain it", () => {
          const roleMenuMap: IRoleMenuMap = { id: 123 };
          const roleMenuMapCollection: IRoleMenuMap[] = [{ id: 456 }];
          expectedResult = service.addRoleMenuMapToCollectionIfMissing(roleMenuMapCollection, roleMenuMap);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roleMenuMap);
        });

        it('should add only unique RoleMenuMap to an array', () => {
          const roleMenuMapArray: IRoleMenuMap[] = [{ id: 123 }, { id: 456 }, { id: 2123 }];
          const roleMenuMapCollection: IRoleMenuMap[] = [{ id: 123 }];
          expectedResult = service.addRoleMenuMapToCollectionIfMissing(roleMenuMapCollection, ...roleMenuMapArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const roleMenuMap: IRoleMenuMap = { id: 123 };
          const roleMenuMap2: IRoleMenuMap = { id: 456 };
          expectedResult = service.addRoleMenuMapToCollectionIfMissing([], roleMenuMap, roleMenuMap2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roleMenuMap);
          expect(expectedResult).toContain(roleMenuMap2);
        });

        it('should accept null and undefined values', () => {
          const roleMenuMap: IRoleMenuMap = { id: 123 };
          expectedResult = service.addRoleMenuMapToCollectionIfMissing([], null, roleMenuMap, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roleMenuMap);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
