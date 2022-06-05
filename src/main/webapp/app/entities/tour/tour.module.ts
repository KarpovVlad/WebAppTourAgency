import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TourComponent } from './list/tour.component';
import { TourDetailComponent } from './detail/tour-detail.component';
import { TourUpdateComponent } from './update/tour-update.component';
import { TourDeleteDialogComponent } from './delete/tour-delete-dialog.component';
import { TourRoutingModule } from './route/tour-routing.module';

@NgModule({
  imports: [SharedModule, TourRoutingModule],
  declarations: [TourComponent, TourDetailComponent, TourUpdateComponent, TourDeleteDialogComponent],
  entryComponents: [TourDeleteDialogComponent],
})
export class TourModule {}
