import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DeliveryService from '@/entities/delivery/delivery.service';
import { IDelivery } from '@/shared/model/delivery.model';

import CustomerService from '@/entities/customer/customer.service';
import { ICustomer } from '@/shared/model/customer.model';

import { ICustomerAddress, CustomerAddress } from '@/shared/model/customer-address.model';
import CustomerAddressService from './customer-address.service';

const validations: any = {
  customerAddress: {
    addressName: {
      required,
    },
    adress: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class CustomerAddressUpdate extends Vue {
  @Inject('customerAddressService') private customerAddressService: () => CustomerAddressService;
  @Inject('alertService') private alertService: () => AlertService;

  public customerAddress: ICustomerAddress = new CustomerAddress();

  @Inject('deliveryService') private deliveryService: () => DeliveryService;

  public deliveries: IDelivery[] = [];

  @Inject('customerService') private customerService: () => CustomerService;

  public customers: ICustomer[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.customerAddressId) {
        vm.retrieveCustomerAddress(to.params.customerAddressId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.customerAddress.id) {
      this.customerAddressService()
        .update(this.customerAddress)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A CustomerAddress is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.customerAddressService()
        .create(this.customerAddress)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A CustomerAddress is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveCustomerAddress(customerAddressId): void {
    this.customerAddressService()
      .find(customerAddressId)
      .then(res => {
        this.customerAddress = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.deliveryService()
      .retrieve()
      .then(res => {
        this.deliveries = res.data;
      });
    this.customerService()
      .retrieve()
      .then(res => {
        this.customers = res.data;
      });
  }
}
