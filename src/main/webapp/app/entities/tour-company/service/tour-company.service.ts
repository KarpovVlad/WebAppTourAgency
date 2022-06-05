import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITourCompany, getTourCompanyIdentifier } from '../tour-company.model';

export type EntityResponseType = HttpResponse<ITourCompany>;
export type EntityArrayResponseType = HttpResponse<ITourCompany[]>;

@Injectable({ providedIn: 'root' })
export class TourCompanyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tour-companies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tourCompany: ITourCompany): Observable<EntityResponseType> {
    return this.http.post<ITourCompany>(this.resourceUrl, tourCompany, { observe: 'response' });
  }

  update(tourCompany: ITourCompany): Observable<EntityResponseType> {
    return this.http.put<ITourCompany>(`${this.resourceUrl}/${getTourCompanyIdentifier(tourCompany) as number}`, tourCompany, {
      observe: 'response',
    });
  }

  partialUpdate(tourCompany: ITourCompany): Observable<EntityResponseType> {
    return this.http.patch<ITourCompany>(`${this.resourceUrl}/${getTourCompanyIdentifier(tourCompany) as number}`, tourCompany, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITourCompany>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITourCompany[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTourCompanyToCollectionIfMissing(
    tourCompanyCollection: ITourCompany[],
    ...tourCompaniesToCheck: (ITourCompany | null | undefined)[]
  ): ITourCompany[] {
    const tourCompanies: ITourCompany[] = tourCompaniesToCheck.filter(isPresent);
    if (tourCompanies.length > 0) {
      const tourCompanyCollectionIdentifiers = tourCompanyCollection.map(tourCompanyItem => getTourCompanyIdentifier(tourCompanyItem)!);
      const tourCompaniesToAdd = tourCompanies.filter(tourCompanyItem => {
        const tourCompanyIdentifier = getTourCompanyIdentifier(tourCompanyItem);
        if (tourCompanyIdentifier == null || tourCompanyCollectionIdentifiers.includes(tourCompanyIdentifier)) {
          return false;
        }
        tourCompanyCollectionIdentifiers.push(tourCompanyIdentifier);
        return true;
      });
      return [...tourCompaniesToAdd, ...tourCompanyCollection];
    }
    return tourCompanyCollection;
  }
}
