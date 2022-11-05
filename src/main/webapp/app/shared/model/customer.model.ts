import { IUser } from '@/shared/model/user.model';
import { ICustomerAddress } from '@/shared/model/customer-address.model';

export interface ICustomer {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  user?: IUser | null;
  customerAddresses?: ICustomerAddress[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public user?: IUser | null,
    public customerAddresses?: ICustomerAddress[] | null
  ) {}
}
