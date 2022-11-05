/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MealComponent from '@/entities/meal/meal.vue';
import MealClass from '@/entities/meal/meal.component';
import MealService from '@/entities/meal/meal.service';
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
  describe('Meal Management Component', () => {
    let wrapper: Wrapper<MealClass>;
    let comp: MealClass;
    let mealServiceStub: SinonStubbedInstance<MealService>;

    beforeEach(() => {
      mealServiceStub = sinon.createStubInstance<MealService>(MealService);
      mealServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MealClass>(MealComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          mealService: () => mealServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      mealServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMeals();
      await comp.$nextTick();

      // THEN
      expect(mealServiceStub.retrieve.called).toBeTruthy();
      expect(comp.meals[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      mealServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(mealServiceStub.retrieve.callCount).toEqual(1);

      comp.removeMeal();
      await comp.$nextTick();

      // THEN
      expect(mealServiceStub.delete.called).toBeTruthy();
      expect(mealServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
