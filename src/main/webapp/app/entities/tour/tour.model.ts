import { ICategory } from 'app/entities/category/category.model';
import { ITourCompany } from 'app/entities/tour-company/tour-company.model';

export interface ITour {
  id?: number;
  name?: string | null;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  price?: number | null;
  persons?: number | null;
  hot?: boolean | null;
  discoint?: number | null;
  categories?: ICategory[] | null;
  tourCompany?: ITourCompany | null;
}

export class Tour implements ITour {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public price?: number | null,
    public persons?: number | null,
    public hot?: boolean | null,
    public discoint?: number | null,
    public categories?: ICategory[] | null,
    public tourCompany?: ITourCompany | null
  ) {
    this.hot = this.hot ?? false;
  }
}

export function getTourIdentifier(tour: ITour): number | undefined {
  return tour.id;
}
