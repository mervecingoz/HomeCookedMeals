<template>
  <div class="home row">
    <div class="col-md-3">
      <span class="hipster img-fluid rounded"></span>
    </div>
    <div class="col-md-9">
      <h1 class="display-4">Home Cooked Meals</h1>
      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username">You are logged in as user "{{ username }}"</span>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span>Please </span>
          <a class="alert-link" v-on:click="openLogin()">sign in</a>
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span>You don't have an account yet?</span>&nbsp;
          <router-link class="alert-link" to="/register">Register a new account</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
