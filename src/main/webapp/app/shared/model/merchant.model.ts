import { IUser } from '@/shared/model/user.model';
import { IMealEntry } from '@/shared/model/meal-entry.model';

export interface IMerchant {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  rating?: number | null;
  user?: IUser | null;
  mealEntries?: IMealEntry[] | null;
}

export class Merchant implements IMerchant {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public rating?: number | null,
    public user?: IUser | null,
    public mealEntries?: IMealEntry[] | null
  ) {}
}
