import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMetaData, MetaData } from '../meta-data.model';

import { MetaDataService } from './meta-data.service';

describe('Service Tests', () => {
  describe('MetaData Service', () => {
    let service: MetaDataService;
    let httpMock: HttpTestingController;
    let elemDefault: IMetaData;
    let expectedResult: IMetaData | IMetaData[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MetaDataService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        headerId: 0,
        fieldName: 'AAAAAAA',
        fieldType: 'AAAAAAA',
        isRequired: 'AAAAAAA',
        fieldOrder: 0,
        fieldValue: 'AAAAAAA',
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

      it('should create a MetaData', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MetaData()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MetaData', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            headerId: 1,
            fieldName: 'BBBBBB',
            fieldType: 'BBBBBB',
            isRequired: 'BBBBBB',
            fieldOrder: 1,
            fieldValue: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MetaData', () => {
        const patchObject = Object.assign(
          {
            headerId: 1,
            fieldType: 'BBBBBB',
            fieldOrder: 1,
            fieldValue: 'BBBBBB',
          },
          new MetaData()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MetaData', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            headerId: 1,
            fieldName: 'BBBBBB',
            fieldType: 'BBBBBB',
            isRequired: 'BBBBBB',
            fieldOrder: 1,
            fieldValue: 'BBBBBB',
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

      it('should delete a MetaData', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMetaDataToCollectionIfMissing', () => {
        it('should add a MetaData to an empty array', () => {
          const metaData: IMetaData = { id: 123 };
          expectedResult = service.addMetaDataToCollectionIfMissing([], metaData);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(metaData);
        });

        it('should not add a MetaData to an array that contains it', () => {
          const metaData: IMetaData = { id: 123 };
          const metaDataCollection: IMetaData[] = [
            {
              ...metaData,
            },
            { id: 456 },
          ];
          expectedResult = service.addMetaDataToCollectionIfMissing(metaDataCollection, metaData);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MetaData to an array that doesn't contain it", () => {
          const metaData: IMetaData = { id: 123 };
          const metaDataCollection: IMetaData[] = [{ id: 456 }];
          expectedResult = service.addMetaDataToCollectionIfMissing(metaDataCollection, metaData);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(metaData);
        });

        it('should add only unique MetaData to an array', () => {
          const metaDataArray: IMetaData[] = [{ id: 123 }, { id: 456 }, { id: 41122 }];
          const metaDataCollection: IMetaData[] = [{ id: 123 }];
          expectedResult = service.addMetaDataToCollectionIfMissing(metaDataCollection, ...metaDataArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const metaData: IMetaData = { id: 123 };
          const metaData2: IMetaData = { id: 456 };
          expectedResult = service.addMetaDataToCollectionIfMissing([], metaData, metaData2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(metaData);
          expect(expectedResult).toContain(metaData2);
        });

        it('should accept null and undefined values', () => {
          const metaData: IMetaData = { id: 123 };
          expectedResult = service.addMetaDataToCollectionIfMissing([], null, metaData, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(metaData);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
