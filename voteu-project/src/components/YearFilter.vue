<script setup lang="ts">
import { type Ref, ref } from "vue";

const year: Ref<string> = ref('');
</script>

<template>
  <div class="main-container">
    <div class="filter-tag">
      <label for="year">Select year</label>
      <select v-model="year">
        <option disabled>---</option>
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
    </div>

    <div class="data-container">
      Showing parties of: {{ year }}
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 1rem;
  border-radius: 8px;
  background-color: white;
  width: 200px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.filter-tag {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 0.5rem;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 6px;
  min-width: 150px;
  font-size: 14px;
  color: #333;
}

select {
  padding: 0.4rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 14px;
}

.data-container {
  margin-top: 1rem;
}
</style>
