import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMealEntry } from '@/shared/model/meal-entry.model';
import MealEntryService from './meal-entry.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MealEntryDetails extends Vue {
  @Inject('mealEntryService') private mealEntryService: () => MealEntryService;
  @Inject('alertService') private alertService: () => AlertService;

  public mealEntry: IMealEntry = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.mealEntryId) {
        vm.retrieveMealEntry(to.params.mealEntryId);
      }
    });
  }

  public retrieveMealEntry(mealEntryId) {
    this.mealEntryService()
      .find(mealEntryId)
      .then(res => {
        this.mealEntry = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
