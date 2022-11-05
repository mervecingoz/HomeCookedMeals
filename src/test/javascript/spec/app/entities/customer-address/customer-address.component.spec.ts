/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CustomerAddressComponent from '@/entities/customer-address/customer-address.vue';
import CustomerAddressClass from '@/entities/customer-address/customer-address.component';
import CustomerAddressService from '@/entities/customer-address/customer-address.service';
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
  describe('CustomerAddress Management Component', () => {
    let wrapper: Wrapper<CustomerAddressClass>;
    let comp: CustomerAddressClass;
    let customerAddressServiceStub: SinonStubbedInstance<CustomerAddressService>;

    beforeEach(() => {
      customerAddressServiceStub = sinon.createStubInstance<CustomerAddressService>(CustomerAddressService);
      customerAddressServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CustomerAddressClass>(CustomerAddressComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          customerAddressService: () => customerAddressServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      customerAddressServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCustomerAddresss();
      await comp.$nextTick();

      // THEN
      expect(customerAddressServiceStub.retrieve.called).toBeTruthy();
      expect(comp.customerAddresses[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      customerAddressServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(customerAddressServiceStub.retrieve.callCount).toEqual(1);

      comp.removeCustomerAddress();
      await comp.$nextTick();

      // THEN
      expect(customerAddressServiceStub.delete.called).toBeTruthy();
      expect(customerAddressServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
