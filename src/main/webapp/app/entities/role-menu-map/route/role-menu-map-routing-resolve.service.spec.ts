jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRoleMenuMap, RoleMenuMap } from '../role-menu-map.model';
import { RoleMenuMapService } from '../service/role-menu-map.service';

import { RoleMenuMapRoutingResolveService } from './role-menu-map-routing-resolve.service';

describe('Service Tests', () => {
  describe('RoleMenuMap routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RoleMenuMapRoutingResolveService;
    let service: RoleMenuMapService;
    let resultRoleMenuMap: IRoleMenuMap | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RoleMenuMapRoutingResolveService);
      service = TestBed.inject(RoleMenuMapService);
      resultRoleMenuMap = undefined;
    });

    describe('resolve', () => {
      it('should return IRoleMenuMap returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoleMenuMap = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoleMenuMap).toEqual({ id: 123 });
      });

      it('should return new IRoleMenuMap if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoleMenuMap = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRoleMenuMap).toEqual(new RoleMenuMap());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoleMenuMap = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoleMenuMap).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
