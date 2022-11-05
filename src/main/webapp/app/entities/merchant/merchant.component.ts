import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMerchant } from '@/shared/model/merchant.model';

import MerchantService from './merchant.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Merchant extends Vue {
  @Inject('merchantService') private merchantService: () => MerchantService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public merchants: IMerchant[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMerchants();
  }

  public clear(): void {
    this.retrieveAllMerchants();
  }

  public retrieveAllMerchants(): void {
    this.isFetching = true;
    this.merchantService()
      .retrieve()
      .then(
        res => {
          this.merchants = res.data;
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

  public prepareRemove(instance: IMerchant): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMerchant(): void {
    this.merchantService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Merchant is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllMerchants();
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
