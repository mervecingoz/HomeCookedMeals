<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="homeCookedMealsApp.meal.home.createOrEditLabel" data-cy="MealCreateUpdateHeading">Create or edit a Meal</h2>
        <div>
          <div class="form-group" v-if="meal.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="meal.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="meal-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="meal-name"
              data-cy="name"
              :class="{ valid: !$v.meal.name.$invalid, invalid: $v.meal.name.$invalid }"
              v-model="$v.meal.name.$model"
              required
            />
            <div v-if="$v.meal.name.$anyDirty && $v.meal.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.meal.name.required"> This field is required. </small>
            </div>
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
            :disabled="$v.meal.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./meal-update.component.ts"></script>
