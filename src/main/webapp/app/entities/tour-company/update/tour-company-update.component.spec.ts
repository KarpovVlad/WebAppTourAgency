import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TourCompanyService } from '../service/tour-company.service';
import { ITourCompany, TourCompany } from '../tour-company.model';

import { TourCompanyUpdateComponent } from './tour-company-update.component';

describe('TourCompany Management Update Component', () => {
  let comp: TourCompanyUpdateComponent;
  let fixture: ComponentFixture<TourCompanyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tourCompanyService: TourCompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TourCompanyUpdateComponent],
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
      .overrideTemplate(TourCompanyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TourCompanyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tourCompanyService = TestBed.inject(TourCompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tourCompany: ITourCompany = { id: 456 };

      activatedRoute.data = of({ tourCompany });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tourCompany));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TourCompany>>();
      const tourCompany = { id: 123 };
      jest.spyOn(tourCompanyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourCompany });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tourCompany }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tourCompanyService.update).toHaveBeenCalledWith(tourCompany);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TourCompany>>();
      const tourCompany = new TourCompany();
      jest.spyOn(tourCompanyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourCompany });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tourCompany }));
      saveSubject.complete();

      // THEN
      expect(tourCompanyService.create).toHaveBeenCalledWith(tourCompany);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TourCompany>>();
      const tourCompany = { id: 123 };
      jest.spyOn(tourCompanyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tourCompany });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tourCompanyService.update).toHaveBeenCalledWith(tourCompany);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
