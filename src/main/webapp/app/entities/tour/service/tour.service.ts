import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITour, getTourIdentifier } from '../tour.model';

export type EntityResponseType = HttpResponse<ITour>;
export type EntityArrayResponseType = HttpResponse<ITour[]>;

@Injectable({ providedIn: 'root' })
export class TourService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tours');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tour: ITour): Observable<EntityResponseType> {
    return this.http.post<ITour>(this.resourceUrl, tour, { observe: 'response' });
  }

  update(tour: ITour): Observable<EntityResponseType> {
    return this.http.put<ITour>(`${this.resourceUrl}/${getTourIdentifier(tour) as number}`, tour, { observe: 'response' });
  }

  partialUpdate(tour: ITour): Observable<EntityResponseType> {
    return this.http.patch<ITour>(`${this.resourceUrl}/${getTourIdentifier(tour) as number}`, tour, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITour>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITour[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTourToCollectionIfMissing(tourCollection: ITour[], ...toursToCheck: (ITour | null | undefined)[]): ITour[] {
    const tours: ITour[] = toursToCheck.filter(isPresent);
    if (tours.length > 0) {
      const tourCollectionIdentifiers = tourCollection.map(tourItem => getTourIdentifier(tourItem)!);
      const toursToAdd = tours.filter(tourItem => {
        const tourIdentifier = getTourIdentifier(tourItem);
        if (tourIdentifier == null || tourCollectionIdentifiers.includes(tourIdentifier)) {
          return false;
        }
        tourCollectionIdentifiers.push(tourIdentifier);
        return true;
      });
      return [...toursToAdd, ...tourCollection];
    }
    return tourCollection;
  }
}
