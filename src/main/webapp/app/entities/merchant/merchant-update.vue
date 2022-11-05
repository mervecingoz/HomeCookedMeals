<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="homeCookedMealsApp.merchant.home.createOrEditLabel" data-cy="MerchantCreateUpdateHeading">Create or edit a Merchant</h2>
        <div>
          <div class="form-group" v-if="merchant.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="merchant.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="merchant-firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="merchant-firstName"
              data-cy="firstName"
              :class="{ valid: !$v.merchant.firstName.$invalid, invalid: $v.merchant.firstName.$invalid }"
              v-model="$v.merchant.firstName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="merchant-lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="merchant-lastName"
              data-cy="lastName"
              :class="{ valid: !$v.merchant.lastName.$invalid, invalid: $v.merchant.lastName.$invalid }"
              v-model="$v.merchant.lastName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="merchant-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="merchant-email"
              data-cy="email"
              :class="{ valid: !$v.merchant.email.$invalid, invalid: $v.merchant.email.$invalid }"
              v-model="$v.merchant.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="merchant-phoneNumber">Phone Number</label>
            <input
              type="text"
              class="form-control"
              name="phoneNumber"
              id="merchant-phoneNumber"
              data-cy="phoneNumber"
              :class="{ valid: !$v.merchant.phoneNumber.$invalid, invalid: $v.merchant.phoneNumber.$invalid }"
              v-model="$v.merchant.phoneNumber.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="merchant-rating">Rating</label>
            <input
              type="number"
              class="form-control"
              name="rating"
              id="merchant-rating"
              data-cy="rating"
              :class="{ valid: !$v.merchant.rating.$invalid, invalid: $v.merchant.rating.$invalid }"
              v-model.number="$v.merchant.rating.$model"
            />
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
            :disabled="$v.merchant.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./merchant-update.component.ts"></script>
