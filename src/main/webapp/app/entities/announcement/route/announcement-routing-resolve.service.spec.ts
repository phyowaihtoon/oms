jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAnnouncement, Announcement } from '../announcement.model';
import { AnnouncementService } from '../service/announcement.service';

import { AnnouncementRoutingResolveService } from './announcement-routing-resolve.service';

describe('Service Tests', () => {
  describe('Announcement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AnnouncementRoutingResolveService;
    let service: AnnouncementService;
    let resultAnnouncement: IAnnouncement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AnnouncementRoutingResolveService);
      service = TestBed.inject(AnnouncementService);
      resultAnnouncement = undefined;
    });

    describe('resolve', () => {
      it('should return IAnnouncement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnouncement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnnouncement).toEqual({ id: 123 });
      });

      it('should return new IAnnouncement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnouncement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAnnouncement).toEqual(new Announcement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAnnouncement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAnnouncement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
