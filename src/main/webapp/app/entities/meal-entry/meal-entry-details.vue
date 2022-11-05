<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mealEntry">
        <h2 class="jh-entity-heading" data-cy="mealEntryDetailsHeading"><span>MealEntry</span> {{ mealEntry.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Date</span>
          </dt>
          <dd>
            <span>{{ mealEntry.date | formatDate }}</span>
          </dd>
          <dt>
            <span>Quota</span>
          </dt>
          <dd>
            <span>{{ mealEntry.quota }}</span>
          </dd>
          <dt>
            <span>Remaining Quota</span>
          </dt>
          <dd>
            <span>{{ mealEntry.remainingQuota }}</span>
          </dd>
          <dt>
            <span>Merchant</span>
          </dt>
          <dd>
            <div v-if="mealEntry.merchant">
              <router-link :to="{ name: 'MerchantView', params: { merchantId: mealEntry.merchant.id } }">{{
                mealEntry.merchant.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Meal</span>
          </dt>
          <dd>
            <div v-if="mealEntry.meal">
              <router-link :to="{ name: 'MealView', params: { mealId: mealEntry.meal.id } }">{{ mealEntry.meal.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="mealEntry.id"
          :to="{ name: 'MealEntryEdit', params: { mealEntryId: mealEntry.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./meal-entry-details.component.ts"></script>
