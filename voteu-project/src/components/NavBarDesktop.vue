<script setup lang="ts">
import { useAuth } from '@/composables/useAuth'
import { useRouter } from 'vue-router'

const { user, logout, isLoggedIn } = useAuth()
const router = useRouter()

function handleLogout() {
  logout()
  router.push('/login')
}
</script>

<template>
  <aside class="sidenav">
    <nav>
      <ul>
        <li class="link-item"><RouterLink to="/">Home</RouterLink></li>
        <li class="link-item"><RouterLink to="/about">About</RouterLink></li>
        <li class="link-item"><RouterLink to="/how">How</RouterLink></li>
        <li class="link-item"><RouterLink to="/filter">Filter</RouterLink></li>
        <li class="link-item"><RouterLink to="/pollingstations">Pollling Stations</RouterLink></li>
        <li class="link-item"><RouterLink to="/compare">Compare</RouterLink></li>
        <li class="link-item"><RouterLink to="/forum">Forum</RouterLink></li>
      </ul>
    </nav>
    <div class="login-link">
      <button v-if="!isLoggedIn()" class="login-button" @click="$router.push('/login')">Login</button>
      <button v-else class="login-button" @click="handleLogout">Logout ({{ user?.username }})</button>
    </div>

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
  list-style-type: none;  /* Remove bullets */
  padding: 0;
  margin: 0;
}

.sidenav .link-item {
  width: 100%;   /* Ensure the div spans the full width */
  display: block; /* Make each <li> behave like a block element */
  margin-bottom: 10px; /* Optional: space between links */
}

.sidenav a {
  display: block;  /* Makes the <RouterLink> fill the container */
  text-decoration: none;  /* No underline */
  color: #333;  /* Default link color */
  padding: 10px;  /* Add padding for better click area */
  width: 100%;
  box-sizing: border-box;/* Ensure it spans the entire width */
}

.sidenav a:hover {
  color: #1976d2;  /* Hover color */
  background-color: #e0e0e0; /* Optional: highlight background on hover */
}
.login-button {
  background-color: #002b80;
  color: white;
  border: none;
  padding: 10px;
  width: 100%;
  cursor: pointer;
  font-weight: bold;
  border-radius: 4px;
}

.login-button:hover {
  background-color: #0040ff;
}
</style>

