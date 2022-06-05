import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tour-company',
        data: { pageTitle: 'TourCompanies' },
        loadChildren: () => import('./tour-company/tour-company.module').then(m => m.TourCompanyModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'tour',
        data: { pageTitle: 'Tours' },
        loadChildren: () => import('./tour/tour.module').then(m => m.TourModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
