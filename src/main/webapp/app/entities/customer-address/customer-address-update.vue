<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="homeCookedMealsApp.customerAddress.home.createOrEditLabel" data-cy="CustomerAddressCreateUpdateHeading">
          Create or edit a CustomerAddress
        </h2>
        <div>
          <div class="form-group" v-if="customerAddress.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="customerAddress.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="customer-address-addressName">Address Name</label>
            <input
              type="text"
              class="form-control"
              name="addressName"
              id="customer-address-addressName"
              data-cy="addressName"
              :class="{ valid: !$v.customerAddress.addressName.$invalid, invalid: $v.customerAddress.addressName.$invalid }"
              v-model="$v.customerAddress.addressName.$model"
              required
            />
            <div v-if="$v.customerAddress.addressName.$anyDirty && $v.customerAddress.addressName.$invalid">
              <small class="form-text text-danger" v-if="!$v.customerAddress.addressName.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="customer-address-adress">Adress</label>
            <input
              type="text"
              class="form-control"
              name="adress"
              id="customer-address-adress"
              data-cy="adress"
              :class="{ valid: !$v.customerAddress.adress.$invalid, invalid: $v.customerAddress.adress.$invalid }"
              v-model="$v.customerAddress.adress.$model"
              required
            />
            <div v-if="$v.customerAddress.adress.$anyDirty && $v.customerAddress.adress.$invalid">
              <small class="form-text text-danger" v-if="!$v.customerAddress.adress.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="customer-address-customer">Customer</label>
            <select
              class="form-control"
              id="customer-address-customer"
              data-cy="customer"
              name="customer"
              v-model="customerAddress.customer"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  customerAddress.customer && customerOption.id === customerAddress.customer.id ? customerAddress.customer : customerOption
                "
                v-for="customerOption in customers"
                :key="customerOption.id"
              >
                {{ customerOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.customerAddress.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./customer-address-update.component.ts"></script>
