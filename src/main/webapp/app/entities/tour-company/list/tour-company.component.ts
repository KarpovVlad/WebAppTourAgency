import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITourCompany } from '../tour-company.model';
import { TourCompanyService } from '../service/tour-company.service';
import { TourCompanyDeleteDialogComponent } from '../delete/tour-company-delete-dialog.component';

@Component({
  selector: 'jhi-tour-company',
  templateUrl: './tour-company.component.html',
})
export class TourCompanyComponent implements OnInit {
  tourCompanies?: ITourCompany[];
  isLoading = false;

  constructor(protected tourCompanyService: TourCompanyService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tourCompanyService.query().subscribe({
      next: (res: HttpResponse<ITourCompany[]>) => {
        this.isLoading = false;
        this.tourCompanies = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITourCompany): number {
    return item.id!;
  }

  delete(tourCompany: ITourCompany): void {
    const modalRef = this.modalService.open(TourCompanyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tourCompany = tourCompany;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
