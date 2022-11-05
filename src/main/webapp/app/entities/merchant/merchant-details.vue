<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="merchant">
        <h2 class="jh-entity-heading" data-cy="merchantDetailsHeading"><span>Merchant</span> {{ merchant.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Rating</span>
          </dt>
          <dd>
            <span>{{ merchant.rating }}</span>
          </dd>
          <dt>
            <span>User</span>
          </dt>
          <dd>
            {{ merchant.user ? merchant.user.id : '' }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="merchant.id" :to="{ name: 'MerchantEdit', params: { merchantId: merchant.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./merchant-details.component.ts"></script>
