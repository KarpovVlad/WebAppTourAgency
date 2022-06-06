import { ITour } from 'app/entities/tour/tour.model';

export interface ITourCompany {
  id?: number;
  name?: string | null;
  tours?: ITour[] | null;
}

export class TourCompany implements ITourCompany {
  constructor(public id?: number, public name?: string | null, public tours?: ITour[] | null) {}
}

export function getTourCompanyIdentifier(tourCompany: ITourCompany): number | undefined {
  return tourCompany.id;
}
