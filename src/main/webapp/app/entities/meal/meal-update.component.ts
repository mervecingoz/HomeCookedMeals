import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import MealEntryService from '@/entities/meal-entry/meal-entry.service';
import { IMealEntry } from '@/shared/model/meal-entry.model';

import { IMeal, Meal } from '@/shared/model/meal.model';
import MealService from './meal.service';

const validations: any = {
  meal: {
    name: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class MealUpdate extends Vue {
  @Inject('mealService') private mealService: () => MealService;
  @Inject('alertService') private alertService: () => AlertService;

  public meal: IMeal = new Meal();

  @Inject('mealEntryService') private mealEntryService: () => MealEntryService;

  public mealEntries: IMealEntry[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.mealId) {
        vm.retrieveMeal(to.params.mealId);
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
    if (this.meal.id) {
      this.mealService()
        .update(this.meal)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Meal is updated with identifier ' + param.id;
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
      this.mealService()
        .create(this.meal)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Meal is created with identifier ' + param.id;
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

  public retrieveMeal(mealId): void {
    this.mealService()
      .find(mealId)
      .then(res => {
        this.meal = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.mealEntryService()
      .retrieve()
      .then(res => {
        this.mealEntries = res.data;
      });
  }
}
