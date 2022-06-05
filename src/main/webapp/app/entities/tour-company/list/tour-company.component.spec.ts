import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TourCompanyService } from '../service/tour-company.service';

import { TourCompanyComponent } from './tour-company.component';

describe('TourCompany Management Component', () => {
  let comp: TourCompanyComponent;
  let fixture: ComponentFixture<TourCompanyComponent>;
  let service: TourCompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TourCompanyComponent],
    })
      .overrideTemplate(TourCompanyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TourCompanyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TourCompanyService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tourCompanies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
