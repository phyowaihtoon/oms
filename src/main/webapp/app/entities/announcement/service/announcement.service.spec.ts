import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnnouncement, Announcement } from '../announcement.model';

import { AnnouncementService } from './announcement.service';

describe('Service Tests', () => {
  describe('Announcement Service', () => {
    let service: AnnouncementService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnnouncement;
    let expectedResult: IAnnouncement | IAnnouncement[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnnouncementService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        description: 'AAAAAAA',
        delFlag: 'AAAAAAA',
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

      it('should create a Announcement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Announcement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Announcement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            delFlag: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Announcement', () => {
        const patchObject = Object.assign({}, new Announcement());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Announcement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            delFlag: 'BBBBBB',
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

      it('should delete a Announcement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnnouncementToCollectionIfMissing', () => {
        it('should add a Announcement to an empty array', () => {
          const announcement: IAnnouncement = { id: 123 };
          expectedResult = service.addAnnouncementToCollectionIfMissing([], announcement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(announcement);
        });

        it('should not add a Announcement to an array that contains it', () => {
          const announcement: IAnnouncement = { id: 123 };
          const announcementCollection: IAnnouncement[] = [
            {
              ...announcement,
            },
            { id: 456 },
          ];
          expectedResult = service.addAnnouncementToCollectionIfMissing(announcementCollection, announcement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Announcement to an array that doesn't contain it", () => {
          const announcement: IAnnouncement = { id: 123 };
          const announcementCollection: IAnnouncement[] = [{ id: 456 }];
          expectedResult = service.addAnnouncementToCollectionIfMissing(announcementCollection, announcement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(announcement);
        });

        it('should add only unique Announcement to an array', () => {
          const announcementArray: IAnnouncement[] = [{ id: 123 }, { id: 456 }, { id: 97239 }];
          const announcementCollection: IAnnouncement[] = [{ id: 123 }];
          expectedResult = service.addAnnouncementToCollectionIfMissing(announcementCollection, ...announcementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const announcement: IAnnouncement = { id: 123 };
          const announcement2: IAnnouncement = { id: 456 };
          expectedResult = service.addAnnouncementToCollectionIfMissing([], announcement, announcement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(announcement);
          expect(expectedResult).toContain(announcement2);
        });

        it('should accept null and undefined values', () => {
          const announcement: IAnnouncement = { id: 123 };
          expectedResult = service.addAnnouncementToCollectionIfMissing([], null, announcement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(announcement);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
