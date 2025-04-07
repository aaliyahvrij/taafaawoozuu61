<script setup lang="ts">

import Constituency from "@/components/Data/Constituency.vue";
import YearFilter from '@/components/YearFilter.vue'
</script>

<template>

    <div class="filters-wrapper">
      <YearFilter />
      <Constituency/>
    </div>

</template>

<style scoped>


.filters-wrapper {
  padding: 2rem;
  margin-left: 0;
  margin-top: 1rem;
  display: flex;
  color:black;
  flex-direction: row;
  background-color: #fafafa;
  width: 930px;
  height: 600px;


}
</style>
