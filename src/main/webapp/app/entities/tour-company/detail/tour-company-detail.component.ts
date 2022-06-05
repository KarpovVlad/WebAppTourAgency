import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITourCompany } from '../tour-company.model';

@Component({
  selector: 'jhi-tour-company-detail',
  templateUrl: './tour-company-detail.component.html',
})
export class TourCompanyDetailComponent implements OnInit {
  tourCompany: ITourCompany | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tourCompany }) => {
      this.tourCompany = tourCompany;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
