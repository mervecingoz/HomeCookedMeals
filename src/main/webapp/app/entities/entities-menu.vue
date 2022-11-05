<template>
  <div>
    <b-dropdown-item to="/meal">
      <font-awesome-icon icon="asterisk" />
      <span>Meal</span>
    </b-dropdown-item>
    <b-dropdown-item to="/meal-entry">
      <font-awesome-icon icon="asterisk" />
      <span>Meal Entry</span>
    </b-dropdown-item>
    <b-dropdown-item to="/delivery">
      <font-awesome-icon icon="asterisk" />
      <span>Delivery</span>
    </b-dropdown-item>
    <b-dropdown-item to="/customer-address">
      <font-awesome-icon icon="asterisk" />
      <span>Customer Address</span>
    </b-dropdown-item>
    <b-dropdown-item to="/customer">
      <font-awesome-icon icon="asterisk" />
      <span>Customer</span>
    </b-dropdown-item>
    <b-dropdown-item to="/merchant">
      <font-awesome-icon icon="asterisk" />
      <span>Merchant</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
