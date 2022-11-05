import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import CustomerAddressService from '@/entities/customer-address/customer-address.service';
import { ICustomerAddress } from '@/shared/model/customer-address.model';

import MealEntryService from '@/entities/meal-entry/meal-entry.service';
import { IMealEntry } from '@/shared/model/meal-entry.model';

import { IDelivery, Delivery } from '@/shared/model/delivery.model';
import DeliveryService from './delivery.service';
import { Status } from '@/shared/model/enumerations/status.model';

const validations: any = {
  delivery: {
    status: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class DeliveryUpdate extends Vue {
  @Inject('deliveryService') private deliveryService: () => DeliveryService;
  @Inject('alertService') private alertService: () => AlertService;

  public delivery: IDelivery = new Delivery();

  @Inject('customerAddressService') private customerAddressService: () => CustomerAddressService;

  public customerAddresses: ICustomerAddress[] = [];

  @Inject('mealEntryService') private mealEntryService: () => MealEntryService;

  public mealEntries: IMealEntry[] = [];
  public statusValues: string[] = Object.keys(Status);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.deliveryId) {
        vm.retrieveDelivery(to.params.deliveryId);
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
    if (this.delivery.id) {
      this.deliveryService()
        .update(this.delivery)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Delivery is updated with identifier ' + param.id;
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
      this.deliveryService()
        .create(this.delivery)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Delivery is created with identifier ' + param.id;
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

  public retrieveDelivery(deliveryId): void {
    this.deliveryService()
      .find(deliveryId)
      .then(res => {
        this.delivery = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.customerAddressService()
      .retrieve()
      .then(res => {
        this.customerAddresses = res.data;
      });
    this.mealEntryService()
      .retrieve()
      .then(res => {
        this.mealEntries = res.data;
      });
  }
}
