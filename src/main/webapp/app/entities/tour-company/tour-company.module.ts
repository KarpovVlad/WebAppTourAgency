import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TourCompanyComponent } from './list/tour-company.component';
import { TourCompanyDetailComponent } from './detail/tour-company-detail.component';
import { TourCompanyUpdateComponent } from './update/tour-company-update.component';
import { TourCompanyDeleteDialogComponent } from './delete/tour-company-delete-dialog.component';
import { TourCompanyRoutingModule } from './route/tour-company-routing.module';

@NgModule({
  imports: [SharedModule, TourCompanyRoutingModule],
  declarations: [TourCompanyComponent, TourCompanyDetailComponent, TourCompanyUpdateComponent, TourCompanyDeleteDialogComponent],
  entryComponents: [TourCompanyDeleteDialogComponent],
})
export class TourCompanyModule {}
