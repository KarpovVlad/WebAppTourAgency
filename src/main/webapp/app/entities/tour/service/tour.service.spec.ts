import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITour, Tour } from '../tour.model';

import { TourService } from './tour.service';

describe('Tour Service', () => {
  let service: TourService;
  let httpMock: HttpTestingController;
  let elemDefault: ITour;
  let expectedResult: ITour | ITour[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TourService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      price: 0,
      persons: 0,
      hot: false,
      discoint: 0,
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

    it('should create a Tour', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tour()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tour', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          image: 'BBBBBB',
          price: 1,
          persons: 1,
          hot: true,
          discoint: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tour', () => {
      const patchObject = Object.assign(
        {
          image: 'BBBBBB',
        },
        new Tour()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tour', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          image: 'BBBBBB',
          price: 1,
          persons: 1,
          hot: true,
          discoint: 1,
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

    it('should delete a Tour', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTourToCollectionIfMissing', () => {
      it('should add a Tour to an empty array', () => {
        const tour: ITour = { id: 123 };
        expectedResult = service.addTourToCollectionIfMissing([], tour);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tour);
      });

      it('should not add a Tour to an array that contains it', () => {
        const tour: ITour = { id: 123 };
        const tourCollection: ITour[] = [
          {
            ...tour,
          },
          { id: 456 },
        ];
        expectedResult = service.addTourToCollectionIfMissing(tourCollection, tour);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tour to an array that doesn't contain it", () => {
        const tour: ITour = { id: 123 };
        const tourCollection: ITour[] = [{ id: 456 }];
        expectedResult = service.addTourToCollectionIfMissing(tourCollection, tour);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tour);
      });

      it('should add only unique Tour to an array', () => {
        const tourArray: ITour[] = [{ id: 123 }, { id: 456 }, { id: 34601 }];
        const tourCollection: ITour[] = [{ id: 123 }];
        expectedResult = service.addTourToCollectionIfMissing(tourCollection, ...tourArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tour: ITour = { id: 123 };
        const tour2: ITour = { id: 456 };
        expectedResult = service.addTourToCollectionIfMissing([], tour, tour2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tour);
        expect(expectedResult).toContain(tour2);
      });

      it('should accept null and undefined values', () => {
        const tour: ITour = { id: 123 };
        expectedResult = service.addTourToCollectionIfMissing([], null, tour, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tour);
      });

      it('should return initial array if no Tour is added', () => {
        const tourCollection: ITour[] = [{ id: 123 }];
        expectedResult = service.addTourToCollectionIfMissing(tourCollection, undefined, null);
        expect(expectedResult).toEqual(tourCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
