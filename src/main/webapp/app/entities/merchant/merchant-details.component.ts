import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMerchant } from '@/shared/model/merchant.model';
import MerchantService from './merchant.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MerchantDetails extends Vue {
  @Inject('merchantService') private merchantService: () => MerchantService;
  @Inject('alertService') private alertService: () => AlertService;

  public merchant: IMerchant = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.merchantId) {
        vm.retrieveMerchant(to.params.merchantId);
      }
    });
  }

  public retrieveMerchant(merchantId) {
    this.merchantService()
      .find(merchantId)
      .then(res => {
        this.merchant = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
