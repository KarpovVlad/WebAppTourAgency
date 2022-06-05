import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITourCompany } from '../tour-company.model';
import { TourCompanyService } from '../service/tour-company.service';

@Component({
  templateUrl: './tour-company-delete-dialog.component.html',
})
export class TourCompanyDeleteDialogComponent {
  tourCompany?: ITourCompany;

  constructor(protected tourCompanyService: TourCompanyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tourCompanyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
