<template>
  <div>
    <h2 id="page-heading" data-cy="MerchantHeading">
      <span id="merchant-heading">Merchants</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'MerchantCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-merchant"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Merchant </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && merchants && merchants.length === 0">
      <span>No merchants found</span>
    </div>
    <div class="table-responsive" v-if="merchants && merchants.length > 0">
      <table class="table table-striped" aria-describedby="merchants">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>First Name</span></th>
            <th scope="row"><span>Last Name</span></th>
            <th scope="row"><span>Email</span></th>
            <th scope="row"><span>Phone Number</span></th>
            <th scope="row"><span>Rating</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="merchant in merchants" :key="merchant.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MerchantView', params: { merchantId: merchant.id } }">{{ merchant.id }}</router-link>
            </td>
            <td>{{ merchant.firstName }}</td>
            <td>{{ merchant.lastName }}</td>
            <td>{{ merchant.email }}</td>
            <td>{{ merchant.phoneNumber }}</td>
            <td>{{ merchant.rating }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MerchantView', params: { merchantId: merchant.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MerchantEdit', params: { merchantId: merchant.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(merchant)"
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
        ><span id="homeCookedMealsApp.merchant.delete.question" data-cy="merchantDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-merchant-heading">Are you sure you want to delete this Merchant?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-merchant"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeMerchant()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./merchant.component.ts"></script>
