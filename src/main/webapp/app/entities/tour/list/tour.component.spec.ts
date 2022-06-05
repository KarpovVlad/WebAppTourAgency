import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TourService } from '../service/tour.service';

import { TourComponent } from './tour.component';

describe('Tour Management Component', () => {
  let comp: TourComponent;
  let fixture: ComponentFixture<TourComponent>;
  let service: TourService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TourComponent],
    })
      .overrideTemplate(TourComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TourComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TourService);

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
    expect(comp.tours?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
