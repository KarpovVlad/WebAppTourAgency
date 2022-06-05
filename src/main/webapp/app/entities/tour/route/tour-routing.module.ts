import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TourComponent } from '../list/tour.component';
import { TourDetailComponent } from '../detail/tour-detail.component';
import { TourUpdateComponent } from '../update/tour-update.component';
import { TourRoutingResolveService } from './tour-routing-resolve.service';

const tourRoute: Routes = [
  {
    path: '',
    component: TourComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TourDetailComponent,
    resolve: {
      tour: TourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TourUpdateComponent,
    resolve: {
      tour: TourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TourUpdateComponent,
    resolve: {
      tour: TourRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tourRoute)],
  exports: [RouterModule],
})
export class TourRoutingModule {}
