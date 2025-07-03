<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { Party } from '@/interface/Party.ts'
import pattern from 'patternomaly'
import type { PartyVote } from '@/interface/PartyVote.ts' // ✅ Import patternomaly

Chart.register(...registerables)

const props = defineProps<{
  partyVotes: Party[] | PartyVote[]| null
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'pie'> | null = null

const patternStyles = [
  'square', 'circle', 'diamond', 'triangle', 'zigzag',
  'line', 'dash', 'dot', 'cross', 'plus'
]

function getPatternOrColor(name: string, index: number): CanvasPattern | string {
  // Create a hash-based hue for fallback color
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const hue = hash % 360
  const color = `hsl(${hue}, 70%, 60%)`

  // Cycle through available patterns
  const patternType = patternStyles[index % patternStyles.length]
  return pattern.draw(patternType, color)
}

watchEffect(() => {
  if (props.partyVotes && canvasRef.value) {
    const labels = props.partyVotes.map(p => p.partyName)
    const data = props.partyVotes.map(p => p.votes)
    const backgroundColors = labels.map((name, i) => getPatternOrColor(name, i))

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
