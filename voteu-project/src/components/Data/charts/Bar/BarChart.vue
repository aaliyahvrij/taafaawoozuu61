<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'
import pattern from 'patternomaly'

Chart.register(...registerables)

const props = defineProps<{
  partyVotes: PartyVotesDTO[] | null
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'bar'> | null = null

const patternStyles = [
  'square', 'circle', 'diamond', 'triangle', 'zigzag',
  'line', 'dash', 'dot', 'cross', 'plus'
]

function generateColorFromName(partyName: string): string {
  let hash = 0
  for (let i = 0; i < partyName.length; i++) {
    hash = partyName.charCodeAt(i) + ((hash << 5) - hash)
  }
  const hue = hash % 360
  return `hsl(${hue}, 70%, 60%)`
}

function getPattern(name: string, index: number) {
  const baseColor = generateColorFromName(name)  // Use 'name', not 'partyName'
  const patternType = patternStyles[index % patternStyles.length]
  return pattern.draw(patternType, baseColor)
}

watchEffect(() => {
  if (!canvasRef.value) return

  if (!props.partyVotes || props.partyVotes.length === 0) {
    if (chartInstance) {
      chartInstance.destroy()
      chartInstance = null
    }
    return
  }

  const labels = props.partyVotes.map(p => p.partyName)
  const data = props.partyVotes.map(p => p.votes)
  // Pass both name and index explicitly:
  const backgroundPatterns = labels.map((name, index) => getPattern(name, index))

  const borderColors = labels.map((partyName) => {
    const baseColor = generateColorFromName(partyName)
    return baseColor.replace('60%', '40%')
  })

  const config: ChartConfiguration<'bar', number[], string> = {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: 'Votes',
        data,
        backgroundColor: backgroundPatterns,
        borderColor: borderColors,
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        }
      },
      animation: { duration: 500 }
    }
  }

  if (chartInstance) {
    chartInstance.destroy()
  }

  chartInstance = new Chart(canvasRef.value, config)
})
</script>

<template>
  <div>
    <canvas ref="canvasRef" width="400" height="200"></canvas>
  </div>
</template>
