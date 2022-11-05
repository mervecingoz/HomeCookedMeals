<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="customerAddress">
        <h2 class="jh-entity-heading" data-cy="customerAddressDetailsHeading"><span>CustomerAddress</span> {{ customerAddress.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Address Name</span>
          </dt>
          <dd>
            <span>{{ customerAddress.addressName }}</span>
          </dd>
          <dt>
            <span>Adress</span>
          </dt>
          <dd>
            <span>{{ customerAddress.adress }}</span>
          </dd>
          <dt>
            <span>Customer</span>
          </dt>
          <dd>
            <div v-if="customerAddress.customer">
              <router-link :to="{ name: 'CustomerView', params: { customerId: customerAddress.customer.id } }">{{
                customerAddress.customer.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="customerAddress.id"
          :to="{ name: 'CustomerAddressEdit', params: { customerAddressId: customerAddress.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./customer-address-details.component.ts"></script>
