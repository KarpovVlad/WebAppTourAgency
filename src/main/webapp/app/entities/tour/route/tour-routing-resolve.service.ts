import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITour, Tour } from '../tour.model';
import { TourService } from '../service/tour.service';

@Injectable({ providedIn: 'root' })
export class TourRoutingResolveService implements Resolve<ITour> {
  constructor(protected service: TourService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITour> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tour: HttpResponse<Tour>) => {
          if (tour.body) {
            return of(tour.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tour());
  }
}
