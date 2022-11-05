/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MerchantUpdateComponent from '@/entities/merchant/merchant-update.vue';
import MerchantClass from '@/entities/merchant/merchant-update.component';
import MerchantService from '@/entities/merchant/merchant.service';

import MealEntryService from '@/entities/meal-entry/meal-entry.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Merchant Management Update Component', () => {
    let wrapper: Wrapper<MerchantClass>;
    let comp: MerchantClass;
    let merchantServiceStub: SinonStubbedInstance<MerchantService>;

    beforeEach(() => {
      merchantServiceStub = sinon.createStubInstance<MerchantService>(MerchantService);

      wrapper = shallowMount<MerchantClass>(MerchantUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          merchantService: () => merchantServiceStub,
          alertService: () => new AlertService(),

          mealEntryService: () =>
            sinon.createStubInstance<MealEntryService>(MealEntryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.merchant = entity;
        merchantServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(merchantServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.merchant = entity;
        merchantServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(merchantServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMerchant = { id: 123 };
        merchantServiceStub.find.resolves(foundMerchant);
        merchantServiceStub.retrieve.resolves([foundMerchant]);

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
