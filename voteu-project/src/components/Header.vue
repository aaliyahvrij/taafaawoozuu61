<template>
  <header class="header">
    <div class="logo">VoteU</div>
    <input type="text" placeholder="Search..." class="search" />
  </header>
</template>

<style scoped>
.header {
  position: fixed;        /* Make it stick to the top */
  top: 0;
  left: 0;                /* Start from the left edge */
  right: 0;               /* Stretch all the way to the right */
  width: 100vw;           /* Optional: Full viewport width */
  height: 100px;
  background-color: #880D1E;
  display: flex;
  align-items: center;
  padding: 0 2rem;
  color: white;
  z-index: 1000;          /* Ensure it's above other content */
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
