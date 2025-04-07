<template>
  <header class="header">
    <div class="logo">VoteU</div>
    <input type="text" placeholder="Search..." class="search" />
  </header>
</template>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 220px;
  right: 0;
  height: 60px;
  background-color: #880D1E;
  display: flex;
  align-items: center;
  padding: 0 2rem;
  color: white;
  z-index: 10;
}

.logo {
  font-weight: bold;
  font-size: 1.5rem;
  margin-right: 2rem;
}

.search {
  flex: 1;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
}
</style>
