<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="delivery">
        <h2 class="jh-entity-heading" data-cy="deliveryDetailsHeading"><span>Delivery</span> {{ delivery.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <span>{{ delivery.status }}</span>
          </dd>
          <dt>
            <span>Customer Address</span>
          </dt>
          <dd>
            <div v-if="delivery.customerAddress">
              <router-link :to="{ name: 'CustomerAddressView', params: { customerAddressId: delivery.customerAddress.id } }">{{
                delivery.customerAddress.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Meal Entry</span>
          </dt>
          <dd>
            <div v-if="delivery.mealEntry">
              <router-link :to="{ name: 'MealEntryView', params: { mealEntryId: delivery.mealEntry.id } }">{{
                delivery.mealEntry.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="delivery.id" :to="{ name: 'DeliveryEdit', params: { deliveryId: delivery.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./delivery-details.component.ts"></script>
