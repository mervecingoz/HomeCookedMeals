/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MealUpdateComponent from '@/entities/meal/meal-update.vue';
import MealClass from '@/entities/meal/meal-update.component';
import MealService from '@/entities/meal/meal.service';

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
  describe('Meal Management Update Component', () => {
    let wrapper: Wrapper<MealClass>;
    let comp: MealClass;
    let mealServiceStub: SinonStubbedInstance<MealService>;

    beforeEach(() => {
      mealServiceStub = sinon.createStubInstance<MealService>(MealService);

      wrapper = shallowMount<MealClass>(MealUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          mealService: () => mealServiceStub,
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
        comp.meal = entity;
        mealServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mealServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.meal = entity;
        mealServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mealServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMeal = { id: 123 };
        mealServiceStub.find.resolves(foundMeal);
        mealServiceStub.retrieve.resolves([foundMeal]);

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
