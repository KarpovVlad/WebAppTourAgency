import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TourCompanyComponent } from '../list/tour-company.component';
import { TourCompanyDetailComponent } from '../detail/tour-company-detail.component';
import { TourCompanyUpdateComponent } from '../update/tour-company-update.component';
import { TourCompanyRoutingResolveService } from './tour-company-routing-resolve.service';

const tourCompanyRoute: Routes = [
  {
    path: '',
    component: TourCompanyComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TourCompanyDetailComponent,
    resolve: {
      tourCompany: TourCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TourCompanyUpdateComponent,
    resolve: {
      tourCompany: TourCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TourCompanyUpdateComponent,
    resolve: {
      tourCompany: TourCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tourCompanyRoute)],
  exports: [RouterModule],
})
export class TourCompanyRoutingModule {}
