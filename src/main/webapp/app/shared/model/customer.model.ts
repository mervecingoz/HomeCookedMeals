import { ICustomerAddress } from '@/shared/model/customer-address.model';

export interface ICustomer {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  nickName?: string | null;
  customerAddresses?: ICustomerAddress[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public nickName?: string | null,
    public customerAddresses?: ICustomerAddress[] | null
  ) {}
}
