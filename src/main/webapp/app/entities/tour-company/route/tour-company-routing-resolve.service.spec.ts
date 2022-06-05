import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITourCompany, TourCompany } from '../tour-company.model';
import { TourCompanyService } from '../service/tour-company.service';

import { TourCompanyRoutingResolveService } from './tour-company-routing-resolve.service';

describe('TourCompany routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TourCompanyRoutingResolveService;
  let service: TourCompanyService;
  let resultTourCompany: ITourCompany | undefined;

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
    routingResolveService = TestBed.inject(TourCompanyRoutingResolveService);
    service = TestBed.inject(TourCompanyService);
    resultTourCompany = undefined;
  });

  describe('resolve', () => {
    it('should return ITourCompany returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTourCompany = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTourCompany).toEqual({ id: 123 });
    });

    it('should return new ITourCompany if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTourCompany = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTourCompany).toEqual(new TourCompany());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TourCompany })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTourCompany = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTourCompany).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
