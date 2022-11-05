import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import MealEntryService from '@/entities/meal-entry/meal-entry.service';
import { IMealEntry } from '@/shared/model/meal-entry.model';

import { IMerchant, Merchant } from '@/shared/model/merchant.model';
import MerchantService from './merchant.service';

const validations: any = {
  merchant: {
    rating: {},
  },
};

@Component({
  validations,
})
export default class MerchantUpdate extends Vue {
  @Inject('merchantService') private merchantService: () => MerchantService;
  @Inject('alertService') private alertService: () => AlertService;

  public merchant: IMerchant = new Merchant();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('mealEntryService') private mealEntryService: () => MealEntryService;

  public mealEntries: IMealEntry[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.merchantId) {
        vm.retrieveMerchant(to.params.merchantId);
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
    if (this.merchant.id) {
      this.merchantService()
        .update(this.merchant)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Merchant is updated with identifier ' + param.id;
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
      this.merchantService()
        .create(this.merchant)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Merchant is created with identifier ' + param.id;
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

  public retrieveMerchant(merchantId): void {
    this.merchantService()
      .find(merchantId)
      .then(res => {
        this.merchant = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.mealEntryService()
      .retrieve()
      .then(res => {
        this.mealEntries = res.data;
      });
  }
}
