import { IUser } from '@/shared/model/user.model';
import { IMealEntry } from '@/shared/model/meal-entry.model';

export interface IMerchant {
  id?: number;
  rating?: number | null;
  user?: IUser | null;
  mealEntries?: IMealEntry[] | null;
}

export class Merchant implements IMerchant {
  constructor(public id?: number, public rating?: number | null, public user?: IUser | null, public mealEntries?: IMealEntry[] | null) {}
}
