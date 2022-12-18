import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICodeDefinition } from '../code-definition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../code-definition.test-samples';

import { CodeDefinitionService } from './code-definition.service';

const requireRestSample: ICodeDefinition = {
  ...sampleWithRequiredData,
};

describe('CodeDefinition Service', () => {
  let service: CodeDefinitionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICodeDefinition | ICodeDefinition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CodeDefinitionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CodeDefinition', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const codeDefinition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(codeDefinition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodeDefinition', () => {
      const codeDefinition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(codeDefinition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodeDefinition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodeDefinition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CodeDefinition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCodeDefinitionToCollectionIfMissing', () => {
      it('should add a CodeDefinition to an empty array', () => {
        const codeDefinition: ICodeDefinition = sampleWithRequiredData;
        expectedResult = service.addCodeDefinitionToCollectionIfMissing([], codeDefinition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeDefinition);
      });

      it('should not add a CodeDefinition to an array that contains it', () => {
        const codeDefinition: ICodeDefinition = sampleWithRequiredData;
        const codeDefinitionCollection: ICodeDefinition[] = [
          {
            ...codeDefinition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCodeDefinitionToCollectionIfMissing(codeDefinitionCollection, codeDefinition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodeDefinition to an array that doesn't contain it", () => {
        const codeDefinition: ICodeDefinition = sampleWithRequiredData;
        const codeDefinitionCollection: ICodeDefinition[] = [sampleWithPartialData];
        expectedResult = service.addCodeDefinitionToCollectionIfMissing(codeDefinitionCollection, codeDefinition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeDefinition);
      });

      it('should add only unique CodeDefinition to an array', () => {
        const codeDefinitionArray: ICodeDefinition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const codeDefinitionCollection: ICodeDefinition[] = [sampleWithRequiredData];
        expectedResult = service.addCodeDefinitionToCollectionIfMissing(codeDefinitionCollection, ...codeDefinitionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codeDefinition: ICodeDefinition = sampleWithRequiredData;
        const codeDefinition2: ICodeDefinition = sampleWithPartialData;
        expectedResult = service.addCodeDefinitionToCollectionIfMissing([], codeDefinition, codeDefinition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeDefinition);
        expect(expectedResult).toContain(codeDefinition2);
      });

      it('should accept null and undefined values', () => {
        const codeDefinition: ICodeDefinition = sampleWithRequiredData;
        expectedResult = service.addCodeDefinitionToCollectionIfMissing([], null, codeDefinition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeDefinition);
      });

      it('should return initial array if no CodeDefinition is added', () => {
        const codeDefinitionCollection: ICodeDefinition[] = [sampleWithRequiredData];
        expectedResult = service.addCodeDefinitionToCollectionIfMissing(codeDefinitionCollection, undefined, null);
        expect(expectedResult).toEqual(codeDefinitionCollection);
      });
    });

    describe('compareCodeDefinition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCodeDefinition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCodeDefinition(entity1, entity2);
        const compareResult2 = service.compareCodeDefinition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCodeDefinition(entity1, entity2);
        const compareResult2 = service.compareCodeDefinition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCodeDefinition(entity1, entity2);
        const compareResult2 = service.compareCodeDefinition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
