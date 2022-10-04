jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserRole, UserRole } from '../user-role.model';
import { UserRoleService } from '../service/user-role.service';

import { UserRoleRoutingResolveService } from './user-role-routing-resolve.service';

describe('Service Tests', () => {
  describe('UserRole routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UserRoleRoutingResolveService;
    let service: UserRoleService;
    let resultUserRole: IUserRole | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UserRoleRoutingResolveService);
      service = TestBed.inject(UserRoleService);
      resultUserRole = undefined;
    });

    describe('resolve', () => {
      it('should return IUserRole returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserRole = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserRole).toEqual({ id: 123 });
      });

      it('should return new IUserRole if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserRole = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUserRole).toEqual(new UserRole());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserRole = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserRole).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
