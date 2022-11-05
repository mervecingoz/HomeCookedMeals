/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DeliveryComponent from '@/entities/delivery/delivery.vue';
import DeliveryClass from '@/entities/delivery/delivery.component';
import DeliveryService from '@/entities/delivery/delivery.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
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
  describe('Delivery Management Component', () => {
    let wrapper: Wrapper<DeliveryClass>;
    let comp: DeliveryClass;
    let deliveryServiceStub: SinonStubbedInstance<DeliveryService>;

    beforeEach(() => {
      deliveryServiceStub = sinon.createStubInstance<DeliveryService>(DeliveryService);
      deliveryServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DeliveryClass>(DeliveryComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          deliveryService: () => deliveryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      deliveryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDeliverys();
      await comp.$nextTick();

      // THEN
      expect(deliveryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.deliveries[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      deliveryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(deliveryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.deliveries[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      deliveryServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(deliveryServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      deliveryServiceStub.retrieve.reset();
      deliveryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(deliveryServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.deliveries[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      deliveryServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(deliveryServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDelivery();
      await comp.$nextTick();

      // THEN
      expect(deliveryServiceStub.delete.called).toBeTruthy();
      expect(deliveryServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
