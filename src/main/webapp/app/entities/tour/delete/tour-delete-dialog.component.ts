import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITour } from '../tour.model';
import { TourService } from '../service/tour.service';

@Component({
  templateUrl: './tour-delete-dialog.component.html',
})
export class TourDeleteDialogComponent {
  tour?: ITour;

  constructor(protected tourService: TourService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tourService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
