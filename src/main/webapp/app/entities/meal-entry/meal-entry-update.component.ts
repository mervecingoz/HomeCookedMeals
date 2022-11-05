import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import MealService from '@/entities/meal/meal.service';
import { IMeal } from '@/shared/model/meal.model';

import DeliveryService from '@/entities/delivery/delivery.service';
import { IDelivery } from '@/shared/model/delivery.model';

import MerchantService from '@/entities/merchant/merchant.service';
import { IMerchant } from '@/shared/model/merchant.model';

import { IMealEntry, MealEntry } from '@/shared/model/meal-entry.model';
import MealEntryService from './meal-entry.service';

const validations: any = {
  mealEntry: {
    date: {},
    quota: {},
    remainingQuota: {},
  },
};

@Component({
  validations,
})
export default class MealEntryUpdate extends Vue {
  @Inject('mealEntryService') private mealEntryService: () => MealEntryService;
  @Inject('alertService') private alertService: () => AlertService;

  public mealEntry: IMealEntry = new MealEntry();

  @Inject('mealService') private mealService: () => MealService;

  public meals: IMeal[] = [];

  @Inject('deliveryService') private deliveryService: () => DeliveryService;

  public deliveries: IDelivery[] = [];

  @Inject('merchantService') private merchantService: () => MerchantService;

  public merchants: IMerchant[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.mealEntryId) {
        vm.retrieveMealEntry(to.params.mealEntryId);
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
    if (this.mealEntry.id) {
      this.mealEntryService()
        .update(this.mealEntry)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A MealEntry is updated with identifier ' + param.id;
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
      this.mealEntryService()
        .create(this.mealEntry)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A MealEntry is created with identifier ' + param.id;
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.mealEntry[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.mealEntry[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.mealEntry[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.mealEntry[field] = null;
    }
  }

  public retrieveMealEntry(mealEntryId): void {
    this.mealEntryService()
      .find(mealEntryId)
      .then(res => {
        res.date = new Date(res.date);
        this.mealEntry = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.mealService()
      .retrieve()
      .then(res => {
        this.meals = res.data;
      });
    this.deliveryService()
      .retrieve()
      .then(res => {
        this.deliveries = res.data;
      });
    this.merchantService()
      .retrieve()
      .then(res => {
        this.merchants = res.data;
      });
  }
}
