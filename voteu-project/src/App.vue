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
.layout,
.content-area {
  display: flex;
  overflow: hidden;
}

.content-area,
.router-view-wrapper {
  flex: 1;
}

.layout {
  flex-direction: column;
  height: 100vh;
}

.content-area {
  flex-direction: row;
  height: calc(100vh - 200px); /* 200px height for the header */
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
  overflow-y: auto;
  overflow-x: hidden;
}

/* Mobile: stack vertically and rearrange order */
@media (max-width: 768px) {
  .content-area {
    flex-direction: column;
  }

  .router-view-wrapper {
    order: 0;
  }

  .sidebar {
    order: 1;
    width: 100%;
  }
}
</style>
