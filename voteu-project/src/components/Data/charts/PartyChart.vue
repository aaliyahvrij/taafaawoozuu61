<script setup lang="ts">

import {partyData} from "@/chartData/partyChartConfig.ts";
import {onMounted} from "vue";
import { Chart, registerables, type ChartConfiguration } from 'chart.js';
import { ElectionService } from '@/services/ElectionService.ts'

// Register necessary chart.js components
Chart.register(...registerables);

const props = defineProps<{
  electionId: string
}>();

onMounted(async () => {
  const partyChartId = document.getElementById("partyChart") as HTMLCanvasElement;

  if (partyChartId) {
    const data = await ElectionService.getNationalPartyVotes(props.electionId);
    if (!data) return;

    const labels = Object.values(data).map(p => p.name);
    const votes = Object.values(data).map(p => p.votes);
    const backgroundColors = labels.map(name => generateColorFromName(name));

    partyData.labels = labels;
    partyData.datasets[0].data = votes;
    partyData.datasets[0].backgroundColor = backgroundColors;

    const partyConfig: ChartConfiguration<'pie', number[], string> = {
      type: 'pie',
      data: partyData,
    };

    new Chart(partyChartId, partyConfig);
  }
});

function generateColorFromName(name: string): string {
  let hash = 0;
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash);
  }
  const hue = hash % 360;
  return `hsl(${hue}, 70%, 60%)`;
}

</script>

<template>
  <div>
    <canvas id="partyChart" width="200" height="200"></canvas>
  </div>
</template>

<style scoped>

</style>
