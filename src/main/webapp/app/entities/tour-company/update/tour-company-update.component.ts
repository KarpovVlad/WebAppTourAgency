import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITourCompany, TourCompany } from '../tour-company.model';
import { TourCompanyService } from '../service/tour-company.service';

@Component({
  selector: 'jhi-tour-company-update',
  templateUrl: './tour-company-update.component.html',
})
export class TourCompanyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tourCompanyName: [],
  });

  constructor(protected tourCompanyService: TourCompanyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tourCompany }) => {
      this.updateForm(tourCompany);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tourCompany = this.createFromForm();
    if (tourCompany.id !== undefined) {
      this.subscribeToSaveResponse(this.tourCompanyService.update(tourCompany));
    } else {
      this.subscribeToSaveResponse(this.tourCompanyService.create(tourCompany));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITourCompany>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tourCompany: ITourCompany): void {
    this.editForm.patchValue({
      id: tourCompany.id,
      tourCompanyName: tourCompany.tourCompanyName,
    });
  }

  protected createFromForm(): ITourCompany {
    return {
      ...new TourCompany(),
      id: this.editForm.get(['id'])!.value,
      tourCompanyName: this.editForm.get(['tourCompanyName'])!.value,
    };
  }
}
