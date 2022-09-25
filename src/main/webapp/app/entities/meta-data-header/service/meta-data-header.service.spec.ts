import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMetaDataHeader, MetaDataHeader } from '../meta-data-header.model';

import { MetaDataHeaderService } from './meta-data-header.service';

describe('Service Tests', () => {
  describe('MetaDataHeader Service', () => {
    let service: MetaDataHeaderService;
    let httpMock: HttpTestingController;
    let elemDefault: IMetaDataHeader;
    let expectedResult: IMetaDataHeader | IMetaDataHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MetaDataHeaderService);
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
        const patchObject = Object.assign(
          {
            docTitle: 'BBBBBB',
          },
          new MetaDataHeader()
        );

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

      describe('addMetaDataHeaderToCollectionIfMissing', () => {
        it('should add a MetaDataHeader to an empty array', () => {
          const metaDataHeader: IMetaDataHeader = { id: 123 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], metaDataHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(metaDataHeader);
        });

        it('should not add a MetaDataHeader to an array that contains it', () => {
          const metaDataHeader: IMetaDataHeader = { id: 123 };
          const metaDataHeaderCollection: IMetaDataHeader[] = [
            {
              ...metaDataHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(metaDataHeaderCollection, metaDataHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MetaDataHeader to an array that doesn't contain it", () => {
          const metaDataHeader: IMetaDataHeader = { id: 123 };
          const metaDataHeaderCollection: IMetaDataHeader[] = [{ id: 456 }];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(metaDataHeaderCollection, metaDataHeader);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(metaDataHeader);
        });

        it('should add only unique MetaDataHeader to an array', () => {
          const metaDataHeaderArray: IMetaDataHeader[] = [{ id: 123 }, { id: 456 }, { id: 36620 }];
          const metaDataHeaderCollection: IMetaDataHeader[] = [{ id: 123 }];
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing(metaDataHeaderCollection, ...metaDataHeaderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const metaDataHeader: IMetaDataHeader = { id: 123 };
          const metaDataHeader2: IMetaDataHeader = { id: 456 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], metaDataHeader, metaDataHeader2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(metaDataHeader);
          expect(expectedResult).toContain(metaDataHeader2);
        });

        it('should accept null and undefined values', () => {
          const metaDataHeader: IMetaDataHeader = { id: 123 };
          expectedResult = service.addMetaDataHeaderToCollectionIfMissing([], null, metaDataHeader, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(metaDataHeader);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
