import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDelivery } from '@/shared/model/delivery.model';
import DeliveryService from './delivery.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DeliveryDetails extends Vue {
  @Inject('deliveryService') private deliveryService: () => DeliveryService;
  @Inject('alertService') private alertService: () => AlertService;

  public delivery: IDelivery = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.deliveryId) {
        vm.retrieveDelivery(to.params.deliveryId);
      }
    });
  }

  public retrieveDelivery(deliveryId) {
    this.deliveryService()
      .find(deliveryId)
      .then(res => {
        this.delivery = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
