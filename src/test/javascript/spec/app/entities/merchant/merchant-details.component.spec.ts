/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MerchantDetailComponent from '@/entities/merchant/merchant-details.vue';
import MerchantClass from '@/entities/merchant/merchant-details.component';
import MerchantService from '@/entities/merchant/merchant.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Merchant Management Detail Component', () => {
    let wrapper: Wrapper<MerchantClass>;
    let comp: MerchantClass;
    let merchantServiceStub: SinonStubbedInstance<MerchantService>;

    beforeEach(() => {
      merchantServiceStub = sinon.createStubInstance<MerchantService>(MerchantService);

      wrapper = shallowMount<MerchantClass>(MerchantDetailComponent, {
        store,
        localVue,
        router,
        provide: { merchantService: () => merchantServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMerchant = { id: 123 };
        merchantServiceStub.find.resolves(foundMerchant);

        // WHEN
        comp.retrieveMerchant(123);
        await comp.$nextTick();

        // THEN
        expect(comp.merchant).toBe(foundMerchant);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMerchant = { id: 123 };
        merchantServiceStub.find.resolves(foundMerchant);

        // WHEN
        comp.beforeRouteEnter({ params: { merchantId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.merchant).toBe(foundMerchant);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
