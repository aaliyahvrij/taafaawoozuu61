<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { Party } from '@/interface/Party.ts'

Chart.register(...registerables)

const props = defineProps<{
  partyVotes: Party[] | null
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'pie'> | null = null

function generateColorFromName(name: string): string {
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const hue = hash % 360
  return `hsl(${hue}, 70%, 60%)`
}

watchEffect(() => {
  if (props.partyVotes && canvasRef.value) {
    const labels = props.partyVotes.map(p => p.name)
    const data = props.partyVotes.map(p => p.votes)
    const backgroundColors = labels.map(generateColorFromName)

    const config: ChartConfiguration<'pie', number[], string> = {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          label: 'Votes',
          data,
          backgroundColor: backgroundColors
        }]
      },
      options: {
        responsive: true,
        radius: '70%'
      }
    }


    if (chartInstance) {
      chartInstance.destroy()
    }

    chartInstance = new Chart(canvasRef.value, config)
  }
})
</script>

<template>
  <div>
    <canvas ref="canvasRef" width="200" height="200"></canvas>
  </div>
</template>
