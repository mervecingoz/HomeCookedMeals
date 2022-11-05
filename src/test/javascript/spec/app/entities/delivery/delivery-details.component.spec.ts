/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DeliveryDetailComponent from '@/entities/delivery/delivery-details.vue';
import DeliveryClass from '@/entities/delivery/delivery-details.component';
import DeliveryService from '@/entities/delivery/delivery.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Delivery Management Detail Component', () => {
    let wrapper: Wrapper<DeliveryClass>;
    let comp: DeliveryClass;
    let deliveryServiceStub: SinonStubbedInstance<DeliveryService>;

    beforeEach(() => {
      deliveryServiceStub = sinon.createStubInstance<DeliveryService>(DeliveryService);

      wrapper = shallowMount<DeliveryClass>(DeliveryDetailComponent, {
        store,
        localVue,
        router,
        provide: { deliveryService: () => deliveryServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDelivery = { id: 123 };
        deliveryServiceStub.find.resolves(foundDelivery);

        // WHEN
        comp.retrieveDelivery(123);
        await comp.$nextTick();

        // THEN
        expect(comp.delivery).toBe(foundDelivery);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDelivery = { id: 123 };
        deliveryServiceStub.find.resolves(foundDelivery);

        // WHEN
        comp.beforeRouteEnter({ params: { deliveryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.delivery).toBe(foundDelivery);
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
