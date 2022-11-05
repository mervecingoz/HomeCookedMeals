<template>
  <div>
    <h2 id="page-heading" data-cy="MealEntryHeading">
      <span id="meal-entry-heading">Meal Entries</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'MealEntryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-meal-entry"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Meal Entry </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mealEntries && mealEntries.length === 0">
      <span>No mealEntries found</span>
    </div>
    <div class="table-responsive" v-if="mealEntries && mealEntries.length > 0">
      <table class="table table-striped" aria-describedby="mealEntries">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('date')">
              <span>Date</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('quota')">
              <span>Quota</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'quota'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('remainingQuota')">
              <span>Remaining Quota</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'remainingQuota'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('meal.id')">
              <span>Meal</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'meal.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('merchant.id')">
              <span>Merchant</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'merchant.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mealEntry in mealEntries" :key="mealEntry.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MealEntryView', params: { mealEntryId: mealEntry.id } }">{{ mealEntry.id }}</router-link>
            </td>
            <td>{{ mealEntry.date | formatDate }}</td>
            <td>{{ mealEntry.quota }}</td>
            <td>{{ mealEntry.remainingQuota }}</td>
            <td>
              <div v-if="mealEntry.meal">
                <router-link :to="{ name: 'MealView', params: { mealId: mealEntry.meal.id } }">{{ mealEntry.meal.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="mealEntry.merchant">
                <router-link :to="{ name: 'MerchantView', params: { merchantId: mealEntry.merchant.id } }">{{
                  mealEntry.merchant.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MealEntryView', params: { mealEntryId: mealEntry.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MealEntryEdit', params: { mealEntryId: mealEntry.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(mealEntry)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="homeCookedMealsApp.mealEntry.delete.question" data-cy="mealEntryDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-mealEntry-heading">Are you sure you want to delete this Meal Entry?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-mealEntry"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeMealEntry()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="mealEntries && mealEntries.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./meal-entry.component.ts"></script>
