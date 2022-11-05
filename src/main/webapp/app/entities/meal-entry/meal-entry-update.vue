<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="homeCookedMealsApp.mealEntry.home.createOrEditLabel" data-cy="MealEntryCreateUpdateHeading">Create or edit a MealEntry</h2>
        <div>
          <div class="form-group" v-if="mealEntry.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="mealEntry.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-entry-date">Date</label>
            <div class="d-flex">
              <input
                id="meal-entry-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !$v.mealEntry.date.$invalid, invalid: $v.mealEntry.date.$invalid }"
                :value="convertDateTimeFromServer($v.mealEntry.date.$model)"
                @change="updateInstantField('date', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-entry-quota">Quota</label>
            <input
              type="number"
              class="form-control"
              name="quota"
              id="meal-entry-quota"
              data-cy="quota"
              :class="{ valid: !$v.mealEntry.quota.$invalid, invalid: $v.mealEntry.quota.$invalid }"
              v-model.number="$v.mealEntry.quota.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-entry-remainingQuota">Remaining Quota</label>
            <input
              type="number"
              class="form-control"
              name="remainingQuota"
              id="meal-entry-remainingQuota"
              data-cy="remainingQuota"
              :class="{ valid: !$v.mealEntry.remainingQuota.$invalid, invalid: $v.mealEntry.remainingQuota.$invalid }"
              v-model.number="$v.mealEntry.remainingQuota.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-entry-meal">Meal</label>
            <select class="form-control" id="meal-entry-meal" data-cy="meal" name="meal" v-model="mealEntry.meal">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="mealEntry.meal && mealOption.id === mealEntry.meal.id ? mealEntry.meal : mealOption"
                v-for="mealOption in meals"
                :key="mealOption.id"
              >
                {{ mealOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-entry-merchant">Merchant</label>
            <select class="form-control" id="meal-entry-merchant" data-cy="merchant" name="merchant" v-model="mealEntry.merchant">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="mealEntry.merchant && merchantOption.id === mealEntry.merchant.id ? mealEntry.merchant : merchantOption"
                v-for="merchantOption in merchants"
                :key="merchantOption.id"
              >
                {{ merchantOption.id }}
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
            :disabled="$v.mealEntry.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./meal-entry-update.component.ts"></script>
