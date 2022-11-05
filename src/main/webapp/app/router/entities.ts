import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Meal = () => import('@/entities/meal/meal.vue');
// prettier-ignore
const MealUpdate = () => import('@/entities/meal/meal-update.vue');
// prettier-ignore
const MealDetails = () => import('@/entities/meal/meal-details.vue');
// prettier-ignore
const MealEntry = () => import('@/entities/meal-entry/meal-entry.vue');
// prettier-ignore
const MealEntryUpdate = () => import('@/entities/meal-entry/meal-entry-update.vue');
// prettier-ignore
const MealEntryDetails = () => import('@/entities/meal-entry/meal-entry-details.vue');
// prettier-ignore
const Delivery = () => import('@/entities/delivery/delivery.vue');
// prettier-ignore
const DeliveryUpdate = () => import('@/entities/delivery/delivery-update.vue');
// prettier-ignore
const DeliveryDetails = () => import('@/entities/delivery/delivery-details.vue');
// prettier-ignore
const CustomerAddress = () => import('@/entities/customer-address/customer-address.vue');
// prettier-ignore
const CustomerAddressUpdate = () => import('@/entities/customer-address/customer-address-update.vue');
// prettier-ignore
const CustomerAddressDetails = () => import('@/entities/customer-address/customer-address-details.vue');
// prettier-ignore
const Customer = () => import('@/entities/customer/customer.vue');
// prettier-ignore
const CustomerUpdate = () => import('@/entities/customer/customer-update.vue');
// prettier-ignore
const CustomerDetails = () => import('@/entities/customer/customer-details.vue');
// prettier-ignore
const Merchant = () => import('@/entities/merchant/merchant.vue');
// prettier-ignore
const MerchantUpdate = () => import('@/entities/merchant/merchant-update.vue');
// prettier-ignore
const MerchantDetails = () => import('@/entities/merchant/merchant-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'meal',
      name: 'Meal',
      component: Meal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal/new',
      name: 'MealCreate',
      component: MealUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal/:mealId/edit',
      name: 'MealEdit',
      component: MealUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal/:mealId/view',
      name: 'MealView',
      component: MealDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal-entry',
      name: 'MealEntry',
      component: MealEntry,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal-entry/new',
      name: 'MealEntryCreate',
      component: MealEntryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal-entry/:mealEntryId/edit',
      name: 'MealEntryEdit',
      component: MealEntryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'meal-entry/:mealEntryId/view',
      name: 'MealEntryView',
      component: MealEntryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'delivery',
      name: 'Delivery',
      component: Delivery,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'delivery/new',
      name: 'DeliveryCreate',
      component: DeliveryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'delivery/:deliveryId/edit',
      name: 'DeliveryEdit',
      component: DeliveryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'delivery/:deliveryId/view',
      name: 'DeliveryView',
      component: DeliveryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer-address',
      name: 'CustomerAddress',
      component: CustomerAddress,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer-address/new',
      name: 'CustomerAddressCreate',
      component: CustomerAddressUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer-address/:customerAddressId/edit',
      name: 'CustomerAddressEdit',
      component: CustomerAddressUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer-address/:customerAddressId/view',
      name: 'CustomerAddressView',
      component: CustomerAddressDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer',
      name: 'Customer',
      component: Customer,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/new',
      name: 'CustomerCreate',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/edit',
      name: 'CustomerEdit',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/view',
      name: 'CustomerView',
      component: CustomerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'merchant',
      name: 'Merchant',
      component: Merchant,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'merchant/new',
      name: 'MerchantCreate',
      component: MerchantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'merchant/:merchantId/edit',
      name: 'MerchantEdit',
      component: MerchantUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'merchant/:merchantId/view',
      name: 'MerchantView',
      component: MerchantDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
