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
.sideNav {
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
  box-sizing: border-box;
}

.sideNav ul {
  list-style-type: none; /* Remove bullets */
  padding: 0;
  margin: 0;
}

.sideNav .linkItem {
  width: 100%;
  display: block; /* Make each <li> behave like a block element */
  margin-bottom: 10px;
}

.sideNav a {
  display: block; /* Makes the <RouterLink> fill the container */
  text-decoration: none;
  color: #333; /* Default link color */
  padding: 10px; /* Add padding for better click area */
  width: 100%;
  box-sizing: border-box; /* Ensure it spans the entire width */
}

.sideNav a:hover {
  color: #1976d2;
  background-color: #e0e0e0;
}
</style>
<script setup lang="ts"></script>
