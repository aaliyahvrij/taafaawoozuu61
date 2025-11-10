<template>
  <aside class="sideNav">
    <nav>
      <ul>
        <li class="linkItem">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/about">About</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/how">How</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/register">Register</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/login">Log In</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/filter">Filter</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/compare">Compare</RouterLink>
        </li>
        <li class="linkItem">
          <RouterLink to="/forum">Forum</RouterLink>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<style scoped>
.sideNav,
.sideNav a {
  box-sizing: border-box; /* .sideNav a: Ensure it spans the entire width */
}

.sideNacv .linkItem,
.sideNav a {
  width: 100%;
}

.sideNav a:hover {
  color: #1976d2;
  background-color: #e0e0e0;
}

.sideNav {
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
}

.sideNav a,
.sideNav .linkItem {
  display: block; /*. sideNav a: Makes the <RouterLink> fill the container */
}

.sideNav a {
  text-decoration: none;
  color: #333; /* Default link color */
  padding: 10px; /* Add padding for better click area */
}

.sideNav .linkItem {
  margin-bottom: 10px;
}

.sideNav ul {
  list-style-type: none; /* Remove bullets */
  padding: 0;
  margin: 0;
}
</style>
<script setup lang="ts"></script>
