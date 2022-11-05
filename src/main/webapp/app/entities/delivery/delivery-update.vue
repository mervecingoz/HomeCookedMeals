<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="homeCookedMealsApp.delivery.home.createOrEditLabel" data-cy="DeliveryCreateUpdateHeading">Create or edit a Delivery</h2>
        <div>
          <div class="form-group" v-if="delivery.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="delivery.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="delivery-status">Status</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.delivery.status.$invalid, invalid: $v.delivery.status.$invalid }"
              v-model="$v.delivery.status.$model"
              id="delivery-status"
              data-cy="status"
              required
            >
              <option v-for="status in statusValues" :key="status" v-bind:value="status">{{ status }}</option>
            </select>
            <div v-if="$v.delivery.status.$anyDirty && $v.delivery.status.$invalid">
              <small class="form-text text-danger" v-if="!$v.delivery.status.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="delivery-quantity">Quantity</label>
            <input
              type="number"
              class="form-control"
              name="quantity"
              id="delivery-quantity"
              data-cy="quantity"
              :class="{ valid: !$v.delivery.quantity.$invalid, invalid: $v.delivery.quantity.$invalid }"
              v-model.number="$v.delivery.quantity.$model"
              required
            />
            <div v-if="$v.delivery.quantity.$anyDirty && $v.delivery.quantity.$invalid">
              <small class="form-text text-danger" v-if="!$v.delivery.quantity.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.delivery.quantity.numeric"> This field should be a number. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="delivery-customerAddress">Customer Address</label>
            <select
              class="form-control"
              id="delivery-customerAddress"
              data-cy="customerAddress"
              name="customerAddress"
              v-model="delivery.customerAddress"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  delivery.customerAddress && customerAddressOption.id === delivery.customerAddress.id
                    ? delivery.customerAddress
                    : customerAddressOption
                "
                v-for="customerAddressOption in customerAddresses"
                :key="customerAddressOption.id"
              >
                {{ customerAddressOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="delivery-mealEntry">Meal Entry</label>
            <select class="form-control" id="delivery-mealEntry" data-cy="mealEntry" name="mealEntry" v-model="delivery.mealEntry">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="delivery.mealEntry && mealEntryOption.id === delivery.mealEntry.id ? delivery.mealEntry : mealEntryOption"
                v-for="mealEntryOption in mealEntries"
                :key="mealEntryOption.id"
              >
                {{ mealEntryOption.id }}
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
            :disabled="$v.delivery.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./delivery-update.component.ts"></script>
