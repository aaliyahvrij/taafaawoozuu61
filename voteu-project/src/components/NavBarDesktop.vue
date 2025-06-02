<template>
  <aside class="sidenav">
    <nav>
      <ul>
        <li class="link-item">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/about">About</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/how">How</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/register">Register</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/login">Log In</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/filter">Filter</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/compare">Compare</RouterLink>
        </li>
        <li class="link-item">
          <RouterLink to="/forum">Forum</RouterLink>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<style scoped>
.sidenav {
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
  box-sizing: border-box;
}

.sidenav ul {
  list-style-type: none; /* Remove bullets */
  padding: 0;
  margin: 0;
}

.sidenav .link-item {
  width: 100%;
  display: block; /* Make each <li> behave like a block element */
  margin-bottom: 10px;
}

.sidenav a {
  display: block; /* Makes the <RouterLink> fill the container */
  text-decoration: none;
  color: #333; /* Default link color */
  padding: 10px; /* Add padding for better click area */
  width: 100%;
  box-sizing: border-box; /* Ensure it spans the entire width */
}

.sidenav a:hover {
  color: #1976d2;
  background-color: #e0e0e0;
}
</style>
<script setup lang="ts"></script>
