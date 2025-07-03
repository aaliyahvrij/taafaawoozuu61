<script setup lang="ts">
import { useRoute } from 'vue-router'
import { onMounted, ref } from 'vue'
import type { PollingStation } from '@/interface/PollingStation.ts'
import { PollingStationsService } from '@/services/electiondata/database/PollingStationsService.ts'
import { PollingStationPartyVotesService } from '@/services/electiondata/database/PollingStationPartyVotesService.ts'
import type { PartyVote } from '@/interface/PartyVote.ts'
import BarPartyChart from '@/components/Data/charts/Bar/BarPartyChart.vue'

const route = useRoute()
const pollingStation = ref<PollingStation | null>(null)
const partyVotes = ref<PartyVote[] | null>(null)
const showTable = ref(true)

async function getPollingStation(id: number): Promise<void> {
  try {
    pollingStation.value = await PollingStationsService.getPollingStationById(id)
  } catch (error) {
    console.error(error)
  }
}

async function getPartyVotes(electionId: string, pollingstationId: string): Promise<void>{
  try{
    partyVotes.value = await PollingStationPartyVotesService.getPartyVotes(electionId, pollingstationId)
    console.log(partyVotes.value)
  } catch (error){
    console.error(error)
  }
}

onMounted(async () => {
  const id = Number(route.params.id)
  await getPollingStation(id)
  if (pollingStation.value) {
    await getPartyVotes(pollingStation.value.electionId, pollingStation.value.pollingStationId)
  }
})
</script>
<template>
  <div v-if="pollingStation" class="pollingstation-card">
    <h2 class="name">{{ pollingStation.name }}</h2>
    <p class="zipcode">Zipcode: {{ pollingStation.zipcode }}</p>
    <p class="authority">Authority: {{ pollingStation.authorityName }}</p>
    <p class="votes">Total Votes: {{ pollingStation.votes ?? 'N/A' }}</p>
  </div>

  <div v-else class="loading">Loading polling station info...</div>

  <button @click="showTable = !showTable" class="toggle-btn">
    {{ showTable ? 'Show Chart' : 'Show Table' }}
  </button>

  <div v-if="showTable">
    <table>
      <thead>
      <tr>
        <th>Party Name</th>
        <th>Votes</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="party in partyVotes" :key="party.id">
        <td>{{ party.partyName }}</td>
        <td>{{ party.votes }}</td>
      </tr>
      </tbody>
    </table>
  </div>

  <div v-else>
    <BarPartyChart :party-votes="partyVotes" />
  </div>
</template>

<style scoped>
.pollingstation-card {
  max-width: 500px;
  margin: 2rem auto;
  padding: 1.5rem 2rem;
  background-color: #002970;
  border-radius: 12px;
  color: white;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: default;
}

.pollingstation-card:hover {
  transform: translateY(-5px);
}

.name {
  font-size: 1.8rem;
  margin-bottom: 0.6rem;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
}

.zipcode, .authority, .votes {
  font-size: 1.1rem;
  margin: 0.4rem 0;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
}

.loading {
  text-align: center;
  color: #555;
  margin-top: 2rem;
}

table {
  width: 100%;
  border-collapse: separate;
  margin-top: 1rem;
}
tbody tr:nth-child(even) {
  background-color: #fafafa;
}

th,
td {
  border: 1px solid #ccc;
  padding: 8px 12px;
  text-align: left;
  border-radius: 5px;
}

th{
  height: 50px;
}
</style>
