/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MealDetailComponent from '@/entities/meal/meal-details.vue';
import MealClass from '@/entities/meal/meal-details.component';
import MealService from '@/entities/meal/meal.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Meal Management Detail Component', () => {
    let wrapper: Wrapper<MealClass>;
    let comp: MealClass;
    let mealServiceStub: SinonStubbedInstance<MealService>;

    beforeEach(() => {
      mealServiceStub = sinon.createStubInstance<MealService>(MealService);

      wrapper = shallowMount<MealClass>(MealDetailComponent, {
        store,
        localVue,
        router,
        provide: { mealService: () => mealServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMeal = { id: 123 };
        mealServiceStub.find.resolves(foundMeal);

        // WHEN
        comp.retrieveMeal(123);
        await comp.$nextTick();

        // THEN
        expect(comp.meal).toBe(foundMeal);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMeal = { id: 123 };
        mealServiceStub.find.resolves(foundMeal);

        // WHEN
        comp.beforeRouteEnter({ params: { mealId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.meal).toBe(foundMeal);
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
