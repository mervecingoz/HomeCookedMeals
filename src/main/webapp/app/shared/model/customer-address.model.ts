import { IDelivery } from '@/shared/model/delivery.model';
import { ICustomer } from '@/shared/model/customer.model';

export interface ICustomerAddress {
  id?: number;
  addressName?: string;
  adress?: string;
  deliveries?: IDelivery[] | null;
  customer?: ICustomer | null;
}

export class CustomerAddress implements ICustomerAddress {
  constructor(
    public id?: number,
    public addressName?: string,
    public adress?: string,
    public deliveries?: IDelivery[] | null,
    public customer?: ICustomer | null
  ) {}
}
