import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMetaDataHeader, IMetaData, MetaDataHeader, MetaData } from '../metadata.model';

import { MetaDataService } from './metadata.service';

describe('Service Tests', () => {
  describe('MetaData Service', () => {
    let service: MetaDataService;
    let httpMock: HttpTestingController;
    let elemDefault: IMetaDataHeader;
    let expectedResult: IMetaDataHeader | IMetaDataHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MetaDataService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        docTitle: 'AAAAAAA',
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

      it('should create a MetaDataHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MetaDataHeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MetaDataHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            docTitle: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MetaDataHeader', () => {
        const patchObject = Object.assign({}, new MetaDataHeader());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MetaDataHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            docTitle: 'BBBBBB',
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

      it('should delete a MetaDataHeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMetadataHeaderToCollectionIfMissing', () => {
        it('should add a IMetaDataHeader to an empty array', () => {
          const metadataHeader: IMetaDataHeader = { id: 123 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], metadataHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(metadataHeader);
        });

        it('should not add a IMetaDataHeader to an array that contains it', () => {
          const metadataHeader: IMetaDataHeader = { id: 123 };
          const metadataHeaderCollection: IMetaDataHeader[] = [
            {
              ...metadataHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(metadataHeaderCollection, metadataHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a IMetaDataHeader to an array that doesn't contain it", () => {
          const data: IMetaDataHeader = { id: 123 };
          const dataCollection: IMetaDataHeader[] = [{ id: 456 }];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(dataCollection, data);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(data);
        });

        it('should add only unique IMetaDataHeader to an array', () => {
          const dataArray: IMetaDataHeader[] = [{ id: 123 }, { id: 456 }, { id: 43896 }];
          const dataCollection: IMetaDataHeader[] = [{ id: 123 }];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(dataCollection, ...dataArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const data: IMetaDataHeader = { id: 123 };
          const data2: IMetaDataHeader = { id: 456 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], data, data2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(data);
          expect(expectedResult).toContain(data2);
        });

        it('should accept null and undefined values', () => {
          const data: IMetaDataHeader = { id: 123 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], null, data, undefined);
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
