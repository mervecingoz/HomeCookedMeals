/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MealEntryDetailComponent from '@/entities/meal-entry/meal-entry-details.vue';
import MealEntryClass from '@/entities/meal-entry/meal-entry-details.component';
import MealEntryService from '@/entities/meal-entry/meal-entry.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('MealEntry Management Detail Component', () => {
    let wrapper: Wrapper<MealEntryClass>;
    let comp: MealEntryClass;
    let mealEntryServiceStub: SinonStubbedInstance<MealEntryService>;

    beforeEach(() => {
      mealEntryServiceStub = sinon.createStubInstance<MealEntryService>(MealEntryService);

      wrapper = shallowMount<MealEntryClass>(MealEntryDetailComponent, {
        store,
        localVue,
        router,
        provide: { mealEntryService: () => mealEntryServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMealEntry = { id: 123 };
        mealEntryServiceStub.find.resolves(foundMealEntry);

        // WHEN
        comp.retrieveMealEntry(123);
        await comp.$nextTick();

        // THEN
        expect(comp.mealEntry).toBe(foundMealEntry);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMealEntry = { id: 123 };
        mealEntryServiceStub.find.resolves(foundMealEntry);

        // WHEN
        comp.beforeRouteEnter({ params: { mealEntryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.mealEntry).toBe(foundMealEntry);
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
