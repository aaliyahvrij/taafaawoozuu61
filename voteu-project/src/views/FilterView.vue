<script setup lang="ts">

import YearFilter from "@/components/YearFilter.vue";
</script>

<template>
  <div class="main-filter-container">
    <YearFilter />
  </div>

</template>

<style scoped>
template{
  display: flex;
  align-items: center;
  justify-content: center;
}
.main-filter-container{
  color:black;
  background-color: #fafafa;
  width: 800px;
  height: 600px;
}
</style>
