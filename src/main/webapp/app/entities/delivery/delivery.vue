<template>
  <div>
    <h2 id="page-heading" data-cy="DeliveryHeading">
      <span id="delivery-heading">Deliveries</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DeliveryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-delivery"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Delivery </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && deliveries && deliveries.length === 0">
      <span>No deliveries found</span>
    </div>
    <div class="table-responsive" v-if="deliveries && deliveries.length > 0">
      <table class="table table-striped" aria-describedby="deliveries">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('status')">
              <span>Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('customerAddress.id')">
              <span>Customer Address</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'customerAddress.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('mealEntry.id')">
              <span>Meal Entry</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mealEntry.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="delivery in deliveries" :key="delivery.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DeliveryView', params: { deliveryId: delivery.id } }">{{ delivery.id }}</router-link>
            </td>
            <td>{{ delivery.status }}</td>
            <td>
              <div v-if="delivery.customerAddress">
                <router-link :to="{ name: 'CustomerAddressView', params: { customerAddressId: delivery.customerAddress.id } }">{{
                  delivery.customerAddress.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="delivery.mealEntry">
                <router-link :to="{ name: 'MealEntryView', params: { mealEntryId: delivery.mealEntry.id } }">{{
                  delivery.mealEntry.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DeliveryView', params: { deliveryId: delivery.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DeliveryEdit', params: { deliveryId: delivery.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(delivery)"
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
        ><span id="homeCookedMealsApp.delivery.delete.question" data-cy="deliveryDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-delivery-heading">Are you sure you want to delete this Delivery?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-delivery"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDelivery()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="deliveries && deliveries.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./delivery.component.ts"></script>
