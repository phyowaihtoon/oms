import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICodeDefinition } from '../code-definition.model';
import { CodeDefinitionService } from '../service/code-definition.service';

import { CodeDefinitionRoutingResolveService } from './code-definition-routing-resolve.service';

describe('CodeDefinition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CodeDefinitionRoutingResolveService;
  let service: CodeDefinitionService;
  let resultCodeDefinition: ICodeDefinition | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CodeDefinitionRoutingResolveService);
    service = TestBed.inject(CodeDefinitionService);
    resultCodeDefinition = undefined;
  });

  describe('resolve', () => {
    it('should return ICodeDefinition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeDefinition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodeDefinition).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeDefinition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCodeDefinition).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(
        of(
          new HttpResponse<ICodeDefinition>({ body: null })
        )
      );
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeDefinition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodeDefinition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
