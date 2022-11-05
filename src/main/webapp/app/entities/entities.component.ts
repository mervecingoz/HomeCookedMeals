import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import MealService from './meal/meal.service';
import MealEntryService from './meal-entry/meal-entry.service';
import DeliveryService from './delivery/delivery.service';
import CustomerAddressService from './customer-address/customer-address.service';
import CustomerService from './customer/customer.service';
import MerchantService from './merchant/merchant.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('mealService') private mealService = () => new MealService();
  @Provide('mealEntryService') private mealEntryService = () => new MealEntryService();
  @Provide('deliveryService') private deliveryService = () => new DeliveryService();
  @Provide('customerAddressService') private customerAddressService = () => new CustomerAddressService();
  @Provide('customerService') private customerService = () => new CustomerService();
  @Provide('merchantService') private merchantService = () => new MerchantService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
