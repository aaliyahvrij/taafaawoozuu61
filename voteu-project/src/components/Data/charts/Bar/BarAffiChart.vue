<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { Affiliation } from '@/interfaces'

Chart.register(...registerables)

const props = defineProps<{
  affiVotes: Affiliation[] | null
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'bar'> | null = null

function generateColorFromName(name: string): string {
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const hue = hash % 360
  return `hsl(${hue}, 70%, 60%)`
}

watchEffect(() => {
  if (!canvasRef.value) {
    return
  }
  if (props.affiVotes && props.affiVotes.length > 0) {
    const labels = props.affiVotes.map(affi => affi.name)
    const data = props.affiVotes.map(affi => affi.vvCount)
    const backgroundColors = labels.map(generateColorFromName)
    const config: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: 'Valid Vote Count',
            data,
            backgroundColor: backgroundColors,
            borderColor: backgroundColors.map((c) => c.replace('60%', '40%')),
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1,
            },
          },
        },
        animation: { duration: 500 },
      },
    }
    if (chartInstance) {
      chartInstance.destroy()
    }
    chartInstance = new Chart(canvasRef.value, config)
  } else {
    if (chartInstance) {
      chartInstance.destroy()
      chartInstance = null
    }
  }
})
</script>

<template>
  <div>
    <canvas ref="canvasRef" width="400" height="300"></canvas>
  </div>
</template>
