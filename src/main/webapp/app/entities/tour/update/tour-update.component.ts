import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITour, Tour } from '../tour.model';
import { TourService } from '../service/tour.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ITourCompany } from 'app/entities/tour-company/tour-company.model';
import { TourCompanyService } from 'app/entities/tour-company/service/tour-company.service';

@Component({
  selector: 'jhi-tour-update',
  templateUrl: './tour-update.component.html',
})
export class TourUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  tourCompaniesSharedCollection: ITourCompany[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    image: [],
    imageContentType: [],
    price: [],
    persons: [],
    hot: [],
    discoint: [],
    categories: [],
    tourCompany: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tourService: TourService,
    protected categoryService: CategoryService,
    protected tourCompanyService: TourCompanyService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tour }) => {
      this.updateForm(tour);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('tourAgencyApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tour = this.createFromForm();
    if (tour.id !== undefined) {
      this.subscribeToSaveResponse(this.tourService.update(tour));
    } else {
      this.subscribeToSaveResponse(this.tourService.create(tour));
    }
  }

  trackCategoryById(_index: number, item: ICategory): number {
    return item.id!;
  }

  trackTourCompanyById(_index: number, item: ITourCompany): number {
    return item.id!;
  }

  getSelectedCategory(option: ICategory, selectedVals?: ICategory[]): ICategory {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITour>>): void {
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

  protected updateForm(tour: ITour): void {
    this.editForm.patchValue({
      id: tour.id,
      name: tour.name,
      description: tour.description,
      image: tour.image,
      imageContentType: tour.imageContentType,
      price: tour.price,
      persons: tour.persons,
      hot: tour.hot,
      discoint: tour.discoint,
      categories: tour.categories,
      tourCompany: tour.tourCompany,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      ...(tour.categories ?? [])
    );
    this.tourCompaniesSharedCollection = this.tourCompanyService.addTourCompanyToCollectionIfMissing(
      this.tourCompaniesSharedCollection,
      tour.tourCompany
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, ...(this.editForm.get('categories')!.value ?? []))
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.tourCompanyService
      .query()
      .pipe(map((res: HttpResponse<ITourCompany[]>) => res.body ?? []))
      .pipe(
        map((tourCompanies: ITourCompany[]) =>
          this.tourCompanyService.addTourCompanyToCollectionIfMissing(tourCompanies, this.editForm.get('tourCompany')!.value)
        )
      )
      .subscribe((tourCompanies: ITourCompany[]) => (this.tourCompaniesSharedCollection = tourCompanies));
  }

  protected createFromForm(): ITour {
    return {
      ...new Tour(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      price: this.editForm.get(['price'])!.value,
      persons: this.editForm.get(['persons'])!.value,
      hot: this.editForm.get(['hot'])!.value,
      discoint: this.editForm.get(['discoint'])!.value,
      categories: this.editForm.get(['categories'])!.value,
      tourCompany: this.editForm.get(['tourCompany'])!.value,
    };
  }
}
