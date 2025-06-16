<script setup lang="ts">
import { ref, watch } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { Party } from '@/interface/Party.ts'

Chart.register(...registerables)

const props = defineProps<{
  partyVotes1: Party[] | null
  partyVotes2: Party[] | null
}>()

const chartRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'bar'> | null = null

watch(() => [props.partyVotes1, props.partyVotes2], ([votes1, votes2]) => {
  if (!chartRef.value) return
  if (!votes1 && !votes2) return

  // Unieke partijnamen uit beide datasets
  const labelsSet = new Set<string>()
  if (votes1) votes1.forEach(p => labelsSet.add(p.name))
  if (votes2) votes2.forEach(p => labelsSet.add(p.name))
  const labels = Array.from(labelsSet)

  // Stemmen per partijnaam (0 als niet aanwezig)
  function getVotes(votes: Party[] | null, name: string): number {
    if (!votes) return 0
    const party = votes.find(p => p.name === name)
    return party ? party.votes : 0
  }

  const total1 = votes1?.reduce((sum, p) => sum + p.votes, 0) || 0
  const total2 = votes2?.reduce((sum, p) => sum + p.votes, 0) || 0

  const data = {
    labels,
    datasets: [
      {
        label: 'Filter Set 1',
        data: labels.map(name => getVotes(votes1, name)),
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgb(54, 162, 235)',
        borderWidth: 1,
      },
      {
        label: 'Filter Set 2',
        data: labels.map(name => getVotes(votes2, name)),
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
        borderColor: 'rgb(255, 99, 132)',
        borderWidth: 1,
      }
    ]
  }

  const config: ChartConfiguration<'bar'> = {
    type: 'bar',
    data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: 'Votes'
          }
        }
      },
      plugins: {
        tooltip: {
          callbacks: {
            label: function (context) {
              const datasetIndex = context.datasetIndex
              const value = context.raw as number

              const total = datasetIndex === 0 ? total1 : total2
              const percentage = total ? ((value / total) * 100).toFixed(1) : '0'

              return `${context.dataset.label}: ${value} votes (${percentage}%)`
            }
          }
        }
      }
    }
  }


  if (chartInstance) {
    chartInstance.destroy()
  }

  chartInstance = new Chart(chartRef.value, config)
}, { immediate: true })
</script>

<template>
  <div class="chart-wrapper">
  <canvas ref="chartRef"></canvas>
  </div>
</template>

<style scoped>
.chart-wrapper {
  height: 350px;
  position: relative;
}
canvas {
  width: 100% !important;
  height: 100% !important;
}
</style>
