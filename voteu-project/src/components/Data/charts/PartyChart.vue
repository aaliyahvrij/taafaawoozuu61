<script setup lang="ts">

import {partyData} from "@/chartData/partyChartConfig.ts";
import {onMounted} from "vue";
import { Chart, registerables, type ChartConfiguration } from 'chart.js';

// Register necessary chart.js components
Chart.register(...registerables);

/**\
 * Chart configuration
 */
const partyConfig: ChartConfiguration<'pie', number[], string> = {
  type: 'pie',
  data: partyData
}

/**
 * Initialize chart.js
 */

onMounted(() => {
  const partyChartId = document.getElementById('partyChart') as HTMLCanvasElement;
  if (partyChartId) {
    new Chart(partyChartId, partyConfig); // Initialize chart with the config
  }
});

</script>

<template>
  <div>
    <canvas id="partyChart" width="400" height="400"></canvas>
  </div>
</template>

<style scoped>

</style>
