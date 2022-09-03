import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRepositoryHeader, IRepository, RepositoryHeader, Repository } from '../repository.model';

import { RepositoryService } from './repository.service';

describe('Service Tests', () => {
  describe('Repository Service', () => {
    let service: RepositoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IRepositoryHeader;
    let expectedResult: IRepositoryHeader | IRepositoryHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RepositoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        repositoryName: 'AAAAAAA',
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

      it('should create a RepositoryHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RepositoryHeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RepositoryHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            repositoryName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RepositoryHeader', () => {
        const patchObject = Object.assign({}, new RepositoryHeader());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RepositoryHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            repositoryName: 'BBBBBB',
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

      it('should delete a RepositoryHeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRepositoryHeaderToCollectionIfMissing', () => {
        it('should add a IRepositoryHeader to an empty array', () => {
          const repositoryHeader: IRepositoryHeader = { id: 123 };
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing([], repositoryHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(repositoryHeader);
        });

        it('should not add a IRepositoryHeader to an array that contains it', () => {
          const repositoryHeader: IRepositoryHeader = { id: 123 };
          const repositoryHeaderCollection: IRepositoryHeader[] = [
            {
              ...repositoryHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing(repositoryHeaderCollection, repositoryHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IRepositoryHeader to an array that doesn't contain it", () => {
          const data: IRepositoryHeader = { id: 123 };
          const dataCollection: IRepositoryHeader[] = [{ id: 456 }];
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing(dataCollection, data);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(data);
        });

        it('should add only unique IRepositoryHeader to an array', () => {
          const dataArray: IRepositoryHeader[] = [{ id: 123 }, { id: 456 }, { id: 43896 }];
          const dataCollection: IRepositoryHeader[] = [{ id: 123 }];
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing(dataCollection, ...dataArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const data: IRepositoryHeader = { id: 123 };
          const data2: IRepositoryHeader = { id: 456 };
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing([], data, data2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(data);
          expect(expectedResult).toContain(data2);
        });

        it('should accept null and undefined values', () => {
          const data: IRepositoryHeader = { id: 123 };
          expectedResult = service.addRepositoryHeaderToCollectionIfMissing([], null, data, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(data);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
