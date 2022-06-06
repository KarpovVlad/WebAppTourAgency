import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITourCompany, TourCompany } from '../tour-company.model';

import { TourCompanyService } from './tour-company.service';

describe('TourCompany Service', () => {
  let service: TourCompanyService;
  let httpMock: HttpTestingController;
  let elemDefault: ITourCompany;
  let expectedResult: ITourCompany | ITourCompany[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TourCompanyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a TourCompany', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TourCompany()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TourCompany', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tourCompanyName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TourCompany', () => {
      const patchObject = Object.assign({}, new TourCompany());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TourCompany', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tourCompanyName: 'BBBBBB',
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

    it('should delete a TourCompany', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTourCompanyToCollectionIfMissing', () => {
      it('should add a TourCompany to an empty array', () => {
        const tourCompany: ITourCompany = { id: 123 };
        expectedResult = service.addTourCompanyToCollectionIfMissing([], tourCompany);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tourCompany);
      });

      it('should not add a TourCompany to an array that contains it', () => {
        const tourCompany: ITourCompany = { id: 123 };
        const tourCompanyCollection: ITourCompany[] = [
          {
            ...tourCompany,
          },
          { id: 456 },
        ];
        expectedResult = service.addTourCompanyToCollectionIfMissing(tourCompanyCollection, tourCompany);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TourCompany to an array that doesn't contain it", () => {
        const tourCompany: ITourCompany = { id: 123 };
        const tourCompanyCollection: ITourCompany[] = [{ id: 456 }];
        expectedResult = service.addTourCompanyToCollectionIfMissing(tourCompanyCollection, tourCompany);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tourCompany);
      });

      it('should add only unique TourCompany to an array', () => {
        const tourCompanyArray: ITourCompany[] = [{ id: 123 }, { id: 456 }, { id: 61943 }];
        const tourCompanyCollection: ITourCompany[] = [{ id: 123 }];
        expectedResult = service.addTourCompanyToCollectionIfMissing(tourCompanyCollection, ...tourCompanyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tourCompany: ITourCompany = { id: 123 };
        const tourCompany2: ITourCompany = { id: 456 };
        expectedResult = service.addTourCompanyToCollectionIfMissing([], tourCompany, tourCompany2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tourCompany);
        expect(expectedResult).toContain(tourCompany2);
      });

      it('should accept null and undefined values', () => {
        const tourCompany: ITourCompany = { id: 123 };
        expectedResult = service.addTourCompanyToCollectionIfMissing([], null, tourCompany, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tourCompany);
      });

      it('should return initial array if no TourCompany is added', () => {
        const tourCompanyCollection: ITourCompany[] = [{ id: 123 }];
        expectedResult = service.addTourCompanyToCollectionIfMissing(tourCompanyCollection, undefined, null);
        expect(expectedResult).toEqual(tourCompanyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
