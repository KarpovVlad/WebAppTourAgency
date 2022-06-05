import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITourCompany, TourCompany } from '../tour-company.model';
import { TourCompanyService } from '../service/tour-company.service';

@Injectable({ providedIn: 'root' })
export class TourCompanyRoutingResolveService implements Resolve<ITourCompany> {
  constructor(protected service: TourCompanyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITourCompany> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tourCompany: HttpResponse<TourCompany>) => {
          if (tourCompany.body) {
            return of(tourCompany.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TourCompany());
  }
}
