import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TourService } from '../service/tour.service';
import { ITour, Tour } from '../tour.model';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ITourCompany } from 'app/entities/tour-company/tour-company.model';
import { TourCompanyService } from 'app/entities/tour-company/service/tour-company.service';

import { TourUpdateComponent } from './tour-update.component';

describe('Tour Management Update Component', () => {
  let comp: TourUpdateComponent;
  let fixture: ComponentFixture<TourUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tourService: TourService;
  let categoryService: CategoryService;
  let tourCompanyService: TourCompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TourUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TourUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TourUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tourService = TestBed.inject(TourService);
    categoryService = TestBed.inject(CategoryService);
    tourCompanyService = TestBed.inject(TourCompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const tour: ITour = { id: 456 };
      const categories: ICategory[] = [{ id: 27183 }];
      tour.categories = categories;

      const categoryCollection: ICategory[] = [{ id: 44741 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [...categories];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TourCompany query and add missing value', () => {
      const tour: ITour = { id: 456 };
      const tourCompany: ITourCompany = { id: 12485 };
      tour.tourCompany = tourCompany;

      const tourCompanyCollection: ITourCompany[] = [{ id: 86240 }];
      jest.spyOn(tourCompanyService, 'query').mockReturnValue(of(new HttpResponse({ body: tourCompanyCollection })));
      const additionalTourCompanies = [tourCompany];
      const expectedCollection: ITourCompany[] = [...additionalTourCompanies, ...tourCompanyCollection];
      jest.spyOn(tourCompanyService, 'addTourCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      expect(tourCompanyService.query).toHaveBeenCalled();
      expect(tourCompanyService.addTourCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        tourCompanyCollection,
        ...additionalTourCompanies
      );
      expect(comp.tourCompaniesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tour: ITour = { id: 456 };
      const categories: ICategory = { id: 48404 };
      tour.categories = [categories];
      const tourCompany: ITourCompany = { id: 88807 };
      tour.tourCompany = tourCompany;

      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tour));
      expect(comp.categoriesSharedCollection).toContain(categories);
      expect(comp.tourCompaniesSharedCollection).toContain(tourCompany);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tour>>();
      const tour = { id: 123 };
      jest.spyOn(tourService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tour }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tourService.update).toHaveBeenCalledWith(tour);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tour>>();
      const tour = new Tour();
      jest.spyOn(tourService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tour }));
      saveSubject.complete();

      // THEN
      expect(tourService.create).toHaveBeenCalledWith(tour);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tour>>();
      const tour = { id: 123 };
      jest.spyOn(tourService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tour });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tourService.update).toHaveBeenCalledWith(tour);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoryById', () => {
      it('Should return tracked Category primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTourCompanyById', () => {
      it('Should return tracked TourCompany primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTourCompanyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCategory', () => {
      it('Should return option if no Category is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCategory(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Category for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCategory(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Category is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCategory(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
