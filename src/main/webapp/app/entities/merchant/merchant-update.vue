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
          <div class="form-group">
            <label class="form-control-label" for="merchant-user">User</label>
            <select class="form-control" id="merchant-user" data-cy="user" name="user" v-model="merchant.user">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="merchant.user && userOption.id === merchant.user.id ? merchant.user : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
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
