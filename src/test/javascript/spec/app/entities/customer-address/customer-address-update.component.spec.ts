/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CustomerAddressUpdateComponent from '@/entities/customer-address/customer-address-update.vue';
import CustomerAddressClass from '@/entities/customer-address/customer-address-update.component';
import CustomerAddressService from '@/entities/customer-address/customer-address.service';

import DeliveryService from '@/entities/delivery/delivery.service';

import CustomerService from '@/entities/customer/customer.service';
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
  describe('CustomerAddress Management Update Component', () => {
    let wrapper: Wrapper<CustomerAddressClass>;
    let comp: CustomerAddressClass;
    let customerAddressServiceStub: SinonStubbedInstance<CustomerAddressService>;

    beforeEach(() => {
      customerAddressServiceStub = sinon.createStubInstance<CustomerAddressService>(CustomerAddressService);

      wrapper = shallowMount<CustomerAddressClass>(CustomerAddressUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          customerAddressService: () => customerAddressServiceStub,
          alertService: () => new AlertService(),

          deliveryService: () =>
            sinon.createStubInstance<DeliveryService>(DeliveryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          customerService: () =>
            sinon.createStubInstance<CustomerService>(CustomerService, {
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
        comp.customerAddress = entity;
        customerAddressServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(customerAddressServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.customerAddress = entity;
        customerAddressServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(customerAddressServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCustomerAddress = { id: 123 };
        customerAddressServiceStub.find.resolves(foundCustomerAddress);
        customerAddressServiceStub.retrieve.resolves([foundCustomerAddress]);

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
