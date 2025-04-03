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
  <p v-if="constituencies.length > 0">{{ constituencies[0].name }} </p> <!-- Zorg ervoor dat je toegang hebt tot een specifiek veld zoals name -->
  <p v-else>Loading...</p>
</template>

<style scoped>
/* Add styles if needed */
</style>
