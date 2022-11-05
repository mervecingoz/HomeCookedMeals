/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CustomerAddressDetailComponent from '@/entities/customer-address/customer-address-details.vue';
import CustomerAddressClass from '@/entities/customer-address/customer-address-details.component';
import CustomerAddressService from '@/entities/customer-address/customer-address.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CustomerAddress Management Detail Component', () => {
    let wrapper: Wrapper<CustomerAddressClass>;
    let comp: CustomerAddressClass;
    let customerAddressServiceStub: SinonStubbedInstance<CustomerAddressService>;

    beforeEach(() => {
      customerAddressServiceStub = sinon.createStubInstance<CustomerAddressService>(CustomerAddressService);

      wrapper = shallowMount<CustomerAddressClass>(CustomerAddressDetailComponent, {
        store,
        localVue,
        router,
        provide: { customerAddressService: () => customerAddressServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCustomerAddress = { id: 123 };
        customerAddressServiceStub.find.resolves(foundCustomerAddress);

        // WHEN
        comp.retrieveCustomerAddress(123);
        await comp.$nextTick();

        // THEN
        expect(comp.customerAddress).toBe(foundCustomerAddress);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCustomerAddress = { id: 123 };
        customerAddressServiceStub.find.resolves(foundCustomerAddress);

        // WHEN
        comp.beforeRouteEnter({ params: { customerAddressId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.customerAddress).toBe(foundCustomerAddress);
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
