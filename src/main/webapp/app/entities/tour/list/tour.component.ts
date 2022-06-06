import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITour } from '../tour.model';
import { TourService } from '../service/tour.service';
import { TourDeleteDialogComponent } from '../delete/tour-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tour',
  templateUrl: './tour.component.html',
})
export class TourComponent implements OnInit {
  tours?: ITour[];
  isLoading = false;

  constructor(protected tourService: TourService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tourService.query().subscribe({
      next: (res: HttpResponse<ITour[]>) => {

        this.isLoading = false;
        this.tours = res.body ?? [];
      },
      error: (res) => {
        console.log(res);
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITour): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tour: ITour): void {
    const modalRef = this.modalService.open(TourDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tour = tour;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
