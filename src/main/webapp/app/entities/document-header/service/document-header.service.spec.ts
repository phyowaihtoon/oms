import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentHeader, DocumentHeader } from '../document-header.model';

import { DocumentHeaderService } from './document-header.service';

describe('Service Tests', () => {
  describe('DocumentHeader Service', () => {
    let service: DocumentHeaderService;
    let httpMock: HttpTestingController;
    let elemDefault: IDocumentHeader;
    let expectedResult: IDocumentHeader | IDocumentHeader[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DocumentHeaderService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        metaDataHeaderId: 0,
        fieldNames: 'AAAAAAA',
        fieldValues: 'AAAAAAA',
        repositoryURL: 'AAAAAAA',
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

      it('should create a DocumentHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DocumentHeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DocumentHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            metaDataHeaderId: 1,
            fieldNames: 'BBBBBB',
            fieldValues: 'BBBBBB',
            repositoryURL: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DocumentHeader', () => {
        const patchObject = Object.assign(
          {
            fieldNames: 'BBBBBB',
            fieldValues: 'BBBBBB',
          },
          new DocumentHeader()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DocumentHeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            metaDataHeaderId: 1,
            fieldNames: 'BBBBBB',
            fieldValues: 'BBBBBB',
            repositoryURL: 'BBBBBB',
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

      it('should delete a DocumentHeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDocumentHeaderToCollectionIfMissing', () => {
        it('should add a DocumentHeader to an empty array', () => {
          const documentHeader: IDocumentHeader = { id: 123 };
          expectedResult = service.addDocumentHeaderToCollectionIfMissing([], documentHeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentHeader);
        });

        it('should not add a DocumentHeader to an array that contains it', () => {
          const documentHeader: IDocumentHeader = { id: 123 };
          const documentHeaderCollection: IDocumentHeader[] = [
            {
              ...documentHeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addDocumentHeaderToCollectionIfMissing(documentHeaderCollection, documentHeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DocumentHeader to an array that doesn't contain it", () => {
          const documentHeader: IDocumentHeader = { id: 123 };
          const documentHeaderCollection: IDocumentHeader[] = [{ id: 456 }];
          expectedResult = service.addDocumentHeaderToCollectionIfMissing(documentHeaderCollection, documentHeader);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentHeader);
        });

        it('should add only unique DocumentHeader to an array', () => {
          const documentHeaderArray: IDocumentHeader[] = [{ id: 123 }, { id: 456 }, { id: 85399 }];
          const documentHeaderCollection: IDocumentHeader[] = [{ id: 123 }];
          expectedResult = service.addDocumentHeaderToCollectionIfMissing(documentHeaderCollection, ...documentHeaderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const documentHeader: IDocumentHeader = { id: 123 };
          const documentHeader2: IDocumentHeader = { id: 456 };
          expectedResult = service.addDocumentHeaderToCollectionIfMissing([], documentHeader, documentHeader2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(documentHeader);
          expect(expectedResult).toContain(documentHeader2);
        });

        it('should accept null and undefined values', () => {
          const documentHeader: IDocumentHeader = { id: 123 };
          expectedResult = service.addDocumentHeaderToCollectionIfMissing([], null, documentHeader, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(documentHeader);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
