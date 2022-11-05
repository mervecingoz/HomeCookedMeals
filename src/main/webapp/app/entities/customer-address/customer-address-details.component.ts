import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICustomerAddress } from '@/shared/model/customer-address.model';
import CustomerAddressService from './customer-address.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CustomerAddressDetails extends Vue {
  @Inject('customerAddressService') private customerAddressService: () => CustomerAddressService;
  @Inject('alertService') private alertService: () => AlertService;

  public customerAddress: ICustomerAddress = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.customerAddressId) {
        vm.retrieveCustomerAddress(to.params.customerAddressId);
      }
    });
  }

  public retrieveCustomerAddress(customerAddressId) {
    this.customerAddressService()
      .find(customerAddressId)
      .then(res => {
        this.customerAddress = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
