import { IMealEntry } from '@/shared/model/meal-entry.model';

export interface IMeal {
  id?: number;
  name?: string;
  mealEntries?: IMealEntry[] | null;
}

export class Meal implements IMeal {
  constructor(public id?: number, public name?: string, public mealEntries?: IMealEntry[] | null) {}
}
