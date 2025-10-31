<script setup lang="ts">
import { Header, NavBarDesktop } from '@/components'
</script>

<template>
  <div class="layout">
    <Header />
    <div class="contentArea">
      <NavBarDesktop class="sideBar" />
      <main class="routerViewWrapper">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout,
.contentArea {
  display: flex;
  overflow: hidden;
}

.contentArea,
.routerViewWrapper {
  flex: 1;
}

.layout {
  height: 100vh;
  flex-direction: column;
}

.contentArea {
  flex-direction: row;
  height: calc(100vh - 200px); /* 200px height for the header */
}

/* Default orders (desktop): Sidebar left, content right */
.sideBar {
  order: 0;
  width: 200px;
  background-color: #f4f4f4;
  padding: 1rem;
  box-sizing: border-box;
  list-style-type: none;
}

.routerViewWrapper {
  order: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

/* Mobile: stack vertically and rearrange order */
@media (max-width: 768px) {
  .contentArea {
    flex-direction: column;
  }

  .routerViewWrapper {
    order: 0;
  }

  .sideBar {
    width: 100%;
    order: 1;
  }
}
</style>
