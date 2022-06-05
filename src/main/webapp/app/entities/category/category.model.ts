import { ITour } from 'app/entities/tour/tour.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  excursion?: boolean | null;
  relax?: boolean | null;
  shopping?: boolean | null;
  tours?: ITour[] | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string | null,
    public excursion?: boolean | null,
    public relax?: boolean | null,
    public shopping?: boolean | null,
    public tours?: ITour[] | null
  ) {
    this.excursion = this.excursion ?? false;
    this.relax = this.relax ?? false;
    this.shopping = this.shopping ?? false;
  }
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
