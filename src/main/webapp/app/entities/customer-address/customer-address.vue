<template>
  <div>
    <h2 id="page-heading" data-cy="CustomerAddressHeading">
      <span id="customer-address-heading">Customer Addresses</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'CustomerAddressCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-customer-address"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Customer Address </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && customerAddresses && customerAddresses.length === 0">
      <span>No customerAddresses found</span>
    </div>
    <div class="table-responsive" v-if="customerAddresses && customerAddresses.length > 0">
      <table class="table table-striped" aria-describedby="customerAddresses">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Address Name</span></th>
            <th scope="row"><span>Adress</span></th>
            <th scope="row"><span>Customer</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="customerAddress in customerAddresses" :key="customerAddress.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CustomerAddressView', params: { customerAddressId: customerAddress.id } }">{{
                customerAddress.id
              }}</router-link>
            </td>
            <td>{{ customerAddress.addressName }}</td>
            <td>{{ customerAddress.adress }}</td>
            <td>
              <div v-if="customerAddress.customer">
                <router-link :to="{ name: 'CustomerView', params: { customerId: customerAddress.customer.id } }">{{
                  customerAddress.customer.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CustomerAddressView', params: { customerAddressId: customerAddress.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CustomerAddressEdit', params: { customerAddressId: customerAddress.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(customerAddress)"
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
        ><span id="homeCookedMealsApp.customerAddress.delete.question" data-cy="customerAddressDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-customerAddress-heading">Are you sure you want to delete this Customer Address?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-customerAddress"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeCustomerAddress()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./customer-address.component.ts"></script>
