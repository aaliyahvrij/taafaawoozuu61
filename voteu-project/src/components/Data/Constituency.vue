<script setup lang="ts">
import { ref, onMounted } from "vue";
import ConstituencyService from "@/services/ConstiService.ts";

const constituencies = ref([]);

// Fetch constituencies
const getConstituency = async () => {
  try {
    const response = await ConstituencyService.getConstituency();
    //todo: fix this error
    constituencies.value = response.data;
  } catch (error) {
    console.error("Error fetching constituencies:", error);
  }
};

onMounted(getConstituency);
</script>

<template>
  <p v-if="constituencies.length > 0">{{ constituencies[0] }}</p>
  <p v-else>Loading...</p>
</template>

<style scoped>
/* Add styles if needed */
</style>

