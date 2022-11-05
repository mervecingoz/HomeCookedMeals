import { ICustomerAddress } from '@/shared/model/customer-address.model';
import { IMealEntry } from '@/shared/model/meal-entry.model';

import { Status } from '@/shared/model/enumerations/status.model';
export interface IDelivery {
  id?: number;
  status?: Status;
  quantity?: number;
  customerAddress?: ICustomerAddress | null;
  mealEntry?: IMealEntry | null;
}

export class Delivery implements IDelivery {
  constructor(
    public id?: number,
    public status?: Status,
    public quantity?: number,
    public customerAddress?: ICustomerAddress | null,
    public mealEntry?: IMealEntry | null
  ) {}
}
