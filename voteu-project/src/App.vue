<script setup lang="ts">
import NavBarDesktop from '@/components/NavBarDesktop.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <div class="layout">
    <Header />

    <div class="content-area">
      <NavBarDesktop class="sidebar" />
      <main class="router-view-wrapper">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: row;
  height: calc(100vh - 200px); /* 200px height for the header */
  overflow: hidden;
}

/* Default orders (desktop): Sidebar left, content right */
.sidebar {
  order: 0;
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
  box-sizing: border-box;
  list-style-type: none;
}

.router-view-wrapper {
  order: 1;
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

/* Mobile: stack vertically and rearrange order */
@media (max-width: 768px) {
  .content-area {
    flex-direction: column;
    height: calc(100vh - 200px); /* keep this or adjust if needed */
  }

  .router-view-wrapper {
    order: 0;
    flex: 1 1 auto; /* take 3 parts of available height */
    overflow-y: auto;
  }

  .sidebar {
    order: 1;
    width: 100%;
    flex: 1 1 auto; /* take 1 part of available height */
    overflow-y: auto; /* optional: allow scrolling if content is tall */
  }
}
</style>
