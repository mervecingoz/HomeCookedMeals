import { IMeal } from '@/shared/model/meal.model';
import { IDelivery } from '@/shared/model/delivery.model';
import { IMerchant } from '@/shared/model/merchant.model';

export interface IMealEntry {
  id?: number;
  date?: Date | null;
  quota?: number | null;
  remainingQuota?: number | null;
  meal?: IMeal | null;
  deliveries?: IDelivery[] | null;
  merchant?: IMerchant | null;
}

export class MealEntry implements IMealEntry {
  constructor(
    public id?: number,
    public date?: Date | null,
    public quota?: number | null,
    public remainingQuota?: number | null,
    public meal?: IMeal | null,
    public deliveries?: IDelivery[] | null,
    public merchant?: IMerchant | null
  ) {}
}
