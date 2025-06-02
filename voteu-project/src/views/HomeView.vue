<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ProvinceService } from '@/services/ProvinceService'
import NetherlandsMap from '@/components/NetherlandsMap.vue'

const hovered = ref<string | null>(null)
const years = ['2021', '2023'] // de jaren die je wil optellen

const provinceVotes = ref<Record<number, number>>({})
const provinceNameToId = ref<Record<string, number>>({})

function handleHover(provinceName: string) {
  hovered.value = provinceName
}

function handleLeave() {
  hovered.value = null
}

onMounted(async () => {
  const provinces = await ProvinceService.getProvincesByElection(years[0]) // één jaar is voldoende voor lijst van provincies
  if (!provinces) return

  // Vul map naam → ID
  provinceNameToId.value = Object.values(provinces).reduce((acc, province) => {
    acc[province.name] = province.id
    return acc
  }, {} as Record<string, number>)

  // Haal totaalstemmen op voor elk provincie-ID
  for (const province of Object.values(provinces)) {
    const totalVotes = await ProvinceService.getTotalVotesForProvinceOverYears(province.id, years)
    if (totalVotes !== null) {
      provinceVotes.value[province.id] = totalVotes
    }
  }
})
</script>

<template>
  <main class="home-container">
    <section class="hero">
      <h1>The Dutch Elections of <span>Years 2021 - 2023</span></h1>
      <p>Hover over a province to see vote counts. Click to learn more.</p>
    </section>

    <section class="map-container">
      <div v-if="hovered" class="tooltip">
        {{ hovered }}:
        {{
          provinceVotes[provinceNameToId[hovered]]?.toLocaleString() || 'Loading...'
        }} total votes (2021 + 2023)
      </div>

      <NetherlandsMap @hover="handleHover" @leave="handleLeave" />
    </section>
  </main>
</template>

<style scoped>
.home-container {
  padding: 2rem;
  font-family: sans-serif;
}

.hero {
  text-align: center;
  margin-bottom: 2rem;
}

h1 {
  font-size: 2rem;
  color: #181818;
}

h1 span {
  font-weight: bold;
}

.map-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 2px dashed #ccc;
  background-color: #f9f9f9;
  padding: 1rem;
}

.tooltip {
  margin-top: 1rem;
  text-align: center;
  font-weight: bold;
  background-color: #fff3cd;
  border: 1px solid #ffeeba;
  padding: 0.5rem 1rem;
  border-radius: 5px;
}
</style>
