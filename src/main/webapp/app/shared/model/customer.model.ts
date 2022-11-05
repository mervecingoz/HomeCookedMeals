import { IUser } from '@/shared/model/user.model';
import { ICustomerAddress } from '@/shared/model/customer-address.model';

export interface ICustomer {
  id?: number;
  user?: IUser | null;
  customerAddresses?: ICustomerAddress[] | null;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public user?: IUser | null, public customerAddresses?: ICustomerAddress[] | null) {}
}
