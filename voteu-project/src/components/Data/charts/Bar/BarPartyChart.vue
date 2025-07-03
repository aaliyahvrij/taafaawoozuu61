<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { Chart, registerables, type ChartConfiguration } from 'chart.js'
import type { Party } from '@/interface/Party.ts'
import pattern from 'patternomaly'
import type { PartyVote } from '@/interface/PartyVote.ts'  // <-- Import patternomaly

Chart.register(...registerables)

const props = defineProps<{
  partyVotes: Party[] | null | PartyVote[]
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart<'bar'> | null = null

const patternStyles = [
  'square', 'circle', 'diamond', 'triangle', 'zigzag',
  'line', 'dash', 'dot', 'cross', 'plus'
]

function generateColorFromName(name: string): string {
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const hue = hash % 360
  return `hsl(${hue}, 70%, 60%)`
}

function getPattern(name: string, index: number) {
  const baseColor = generateColorFromName(name)
  const patternType = patternStyles[index % patternStyles.length]
  return pattern.draw(patternType, baseColor)
}

watchEffect(() => {
  if (!canvasRef.value) return

  if (props.partyVotes && props.partyVotes.length > 0) {
    const labels = props.partyVotes.map(p => p.partyName)
    const data = props.partyVotes.map(p => p.votes)
    const backgroundPatterns = labels.map(getPattern)
    // For borders, use a darker shade of base color:
    const borderColors = labels.map((name, i) => {
      const baseColor = generateColorFromName(name)
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
            ticks: {
              stepSize: 1
            }
          }
        },
        animation: { duration: 500 }
      }
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
