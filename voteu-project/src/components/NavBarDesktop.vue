<script setup lang="ts">

import {RouterLink} from "vue-router";
//Todo: make an array of the links and loop through it
</script>

<template>
  <div id=nav-block>
    <div id="navbar" class="navbar">
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/how">How does it work?</RouterLink>
      <RouterLink to="/filter">Filters</RouterLink>
      <RouterLink to="/compare">Compare</RouterLink>
      <RouterLink to="/forum">Forum</RouterLink>
    </div>
  </div>
</template>

<style scoped>

#nav-block {
  position: fixed;
  height: 100vh;
  width: 220px;
  background-color: #f8f8f8;
  top: 0;
  left: 0;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
}

.navbar {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  padding-top: 2rem;
  height: 100%;
  margin-top: 60px /* Offset by the height of your header */
}

.navbar a {
  text-decoration: none;
  color: #333;
  font-weight: 600;
  margin: 1rem 0;
  width: 100%;
  text-align: center;
  padding: 1rem 0;
  min-height: 70px;

}

.navbar a:hover {
  background-color: #d6e8ff;
  color: #002970;
}

</style>
