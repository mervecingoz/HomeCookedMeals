/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MerchantComponent from '@/entities/merchant/merchant.vue';
import MerchantClass from '@/entities/merchant/merchant.component';
import MerchantService from '@/entities/merchant/merchant.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Merchant Management Component', () => {
    let wrapper: Wrapper<MerchantClass>;
    let comp: MerchantClass;
    let merchantServiceStub: SinonStubbedInstance<MerchantService>;

    beforeEach(() => {
      merchantServiceStub = sinon.createStubInstance<MerchantService>(MerchantService);
      merchantServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MerchantClass>(MerchantComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          merchantService: () => merchantServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      merchantServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMerchants();
      await comp.$nextTick();

      // THEN
      expect(merchantServiceStub.retrieve.called).toBeTruthy();
      expect(comp.merchants[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      merchantServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(merchantServiceStub.retrieve.callCount).toEqual(1);

      comp.removeMerchant();
      await comp.$nextTick();

      // THEN
      expect(merchantServiceStub.delete.called).toBeTruthy();
      expect(merchantServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
