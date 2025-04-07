<script setup lang="ts">
import { ref, onMounted } from "vue";
import ConstituencyService from "@/services/ConstituencyService";
import type { Constituency } from "@/services/ConstituencyService";  // Gebruik 'type' om de interface te importeren

const constituencies = ref<Constituency[]>([]);

// Fetch constituencies
const getConstituency = async () => {
  try {
    const response = await ConstituencyService.getConstituency();
    constituencies.value = response.data; // Dit zou nu een array van Constituency moeten zijn
  } catch (error) {
    console.error("Error fetching constituencies:", error);
  }
};

onMounted(getConstituency);
</script>

<template>
  <div class="main-container">
    <div class="filter-tag">
      <label for="constituency">Select Constituency</label>
      <select v-model="Constituency">
        <option value=""></option>
        <option value=""></option>
      </select>
    </div>

    <div class="data-container">

    </div>
  </div>
  <p v-if="constituencies.length > 0">{{ constituencies[0].name }} </p> <!-- Zorg ervoor dat je toegang hebt tot een specifiek veld zoals name -->
  <p v-else>Loading...</p>
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
/* Add styles if needed */
</style>
