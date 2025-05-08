<script setup lang="ts">
import NavBarDesktop from '@/components/NavBarDesktop.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <header>
    <div class="wrapper">
      <Header />
      <nav>
        <NavBarDesktop />
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
html,
body {
  height: 100%;
  margin: 0; /* Remove default margins */
}
</style>
