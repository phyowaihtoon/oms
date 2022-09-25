jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRepository, Repository } from '../repository.model';
import { RepositoryService } from '../service/repository.service';

import { RepositoryRoutingResolveService } from './repository-routing-resolve.service';

describe('Service Tests', () => {
  describe('Repository routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RepositoryRoutingResolveService;
    let service: RepositoryService;
    let resultRepository: IRepository | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RepositoryRoutingResolveService);
      service = TestBed.inject(RepositoryService);
      resultRepository = undefined;
    });

    describe('resolve', () => {
      it('should return IRepository returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRepository = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRepository).toEqual({ id: 123 });
      });

      it('should return new IRepository if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRepository = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRepository).toEqual(new Repository());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRepository = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRepository).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
