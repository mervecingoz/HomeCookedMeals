import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMeal } from '@/shared/model/meal.model';

import MealService from './meal.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Meal extends Vue {
  @Inject('mealService') private mealService: () => MealService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public meals: IMeal[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMeals();
  }

  public clear(): void {
    this.retrieveAllMeals();
  }

  public retrieveAllMeals(): void {
    this.isFetching = true;
    this.mealService()
      .retrieve()
      .then(
        res => {
          this.meals = res.data;
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

  public prepareRemove(instance: IMeal): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMeal(): void {
    this.mealService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Meal is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllMeals();
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
