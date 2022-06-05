import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TourCompanyDetailComponent } from './tour-company-detail.component';

describe('TourCompany Management Detail Component', () => {
  let comp: TourCompanyDetailComponent;
  let fixture: ComponentFixture<TourCompanyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TourCompanyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tourCompany: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TourCompanyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TourCompanyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tourCompany on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tourCompany).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
