/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MealEntryComponent from '@/entities/meal-entry/meal-entry.vue';
import MealEntryClass from '@/entities/meal-entry/meal-entry.component';
import MealEntryService from '@/entities/meal-entry/meal-entry.service';
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
  describe('MealEntry Management Component', () => {
    let wrapper: Wrapper<MealEntryClass>;
    let comp: MealEntryClass;
    let mealEntryServiceStub: SinonStubbedInstance<MealEntryService>;

    beforeEach(() => {
      mealEntryServiceStub = sinon.createStubInstance<MealEntryService>(MealEntryService);
      mealEntryServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MealEntryClass>(MealEntryComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          mealEntryService: () => mealEntryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      mealEntryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMealEntrys();
      await comp.$nextTick();

      // THEN
      expect(mealEntryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.mealEntries[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      mealEntryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(mealEntryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.mealEntries[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      mealEntryServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(mealEntryServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      mealEntryServiceStub.retrieve.reset();
      mealEntryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(mealEntryServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.mealEntries[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      mealEntryServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(mealEntryServiceStub.retrieve.callCount).toEqual(1);

      comp.removeMealEntry();
      await comp.$nextTick();

      // THEN
      expect(mealEntryServiceStub.delete.called).toBeTruthy();
      expect(mealEntryServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
