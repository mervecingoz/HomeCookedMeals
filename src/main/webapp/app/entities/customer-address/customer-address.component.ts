import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICustomerAddress } from '@/shared/model/customer-address.model';

import CustomerAddressService from './customer-address.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class CustomerAddress extends Vue {
  @Inject('customerAddressService') private customerAddressService: () => CustomerAddressService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public customerAddresses: ICustomerAddress[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCustomerAddresss();
  }

  public clear(): void {
    this.retrieveAllCustomerAddresss();
  }

  public retrieveAllCustomerAddresss(): void {
    this.isFetching = true;
    this.customerAddressService()
      .retrieve()
      .then(
        res => {
          this.customerAddresses = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: ICustomerAddress): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCustomerAddress(): void {
    this.customerAddressService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A CustomerAddress is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCustomerAddresss();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
