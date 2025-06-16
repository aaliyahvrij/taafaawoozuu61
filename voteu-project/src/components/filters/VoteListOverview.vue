<script setup lang="ts">
import { PartyStyleService } from '@/services/PartyStyleService.ts'
import PartyChart from '@/components/Data/charts/PartyChart.vue'
import type { Party } from '@/interface/Party.ts'
import type { Candidate } from '@/interface/Candidate.ts'
import BarPartyChart from '@/components/Data/charts/Bar/BarPartyChart.vue'

defineProps<{
  selectedElection: string | number | null
  displayedPartyVotes: Party[] | null
  partyVotes: Party[] | null
  selectedParty: Party | null
  selectedCandidate: Candidate | null
  currentVoteLevel: string | null
}>()

defineEmits<{
  (e: 'select-party', party: Party): void
  (e: 'deselect-party'): void
  (e: 'select-candidate', candidate: Candidate): void
  (e: 'deselect-candidate'): void
  (e: 'sort-candidates-by-name'): void
  (e: 'sort-candidates-by-votes'): void
}>()
</script>

<template>
  <div class="filtered-data">
    <!-- Party list -->
    <div class="party-list" v-if="selectedElection && displayedPartyVotes && !selectedParty">
      <p>{{ currentVoteLevel }} party votes for Election {{ selectedElection }}</p>

      <BarPartyChart v-if="partyVotes" :partyVotes="displayedPartyVotes" />

      <div
          class="party-row"
          v-for="party in displayedPartyVotes"
          :key="party.id"
          @click="$emit('select-party', party)"
          :style="{ backgroundColor: PartyStyleService.generateColorFromName(party.name) }"
      >
        <div class="party-name">{{ party.name }}</div>
        <div class="party-votes">{{ party.votes.toLocaleString() }} votes</div>
        <div class="party-percentage">{{ party.percentage.toFixed(2) }} %</div>
      </div>
    </div>

    <!-- Candidate list -->
    <div v-if="selectedParty && selectedElection && !selectedCandidate">
      <h1 class="party-title">{{ selectedParty.name }}</h1>
      <h2 class="candidate-list-title">Candidates</h2>
      <div class="buttons">
        <button class="back-button" @click="$emit('deselect-party')">Back</button>
        <button class="back-button" @click="$emit('sort-candidates-by-name')">sort by name</button>
        <button class="back-button" @click="$emit('sort-candidates-by-votes')">sort by votes</button>
      </div>

      <div
          class="candidate"
          v-for="candidate in selectedParty.candidates"
          :key="candidate.id"
          @click="$emit('select-candidate', candidate)"
      >
        <p v-if="candidate.firstName && candidate.lastName">
          {{ candidate.firstName }} {{ candidate.lastName }} :
          {{ candidate.votes.toLocaleString() }} votes
        </p>
      </div>
    </div>

    <!-- Candidate details -->
    <div v-if="selectedCandidate && selectedElection" class="candidate-details-card">
      <h2 class="candidate-title" v-if="selectedCandidate.shortCode">{{ selectedCandidate.shortCode }}</h2>
      <h3 class="candidate-name" v-if="selectedCandidate.firstName && selectedCandidate.lastName">
        {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
      </h3>
      <p class="candidate-info">
        <strong>Gender:</strong> {{ selectedCandidate.gender }}
        <svg v-if="selectedCandidate.gender==='male'" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#2854C5"><path d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"/></svg>
        <svg v-if="selectedCandidate.gender==='female'" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#B87E9F"><path d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"/></svg>
        <br />
        <strong>Locality:</strong> {{ selectedCandidate.localityName }}
      </p>
      <p class="candidate-votes">
        Votes: <strong>{{ selectedCandidate.votes.toLocaleString() }}</strong>
      </p>
      <button class="back-button" @click="$emit('deselect-candidate')">Back</button>
    </div>
  </div>
</template>

<style scoped>
.candidate-list-title {
  margin-left: 1rem;
}
.party-title {
  font-size: 2.5rem;
  text-align: center;

}
.candidate-details-card {
  background: #f9fafb;

  border-radius: 12px;
  padding: 24px 32px;
  margin: 24px auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.candidate-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
}

.candidate-name {
  font-size: 1.75rem;
  color: #34495e;
  margin-bottom: 16px;
}

.candidate-info {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 16px;
  line-height: 1.5;
}

.candidate-votes {
  font-size: 1.25rem;
  color: #000000;
  margin-bottom: 24px;
}

.back-button {
  background-color: #002970;
  margin: 1px;
  border: none;
  color: white;
  padding: 10px 24px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

  .party-row, .candidate {
    display: flex;
    align-items: center;
    padding: 0.75rem 1rem;
    background-color: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 0.375rem;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    cursor: pointer;
    justify-content: space-between; /* pushes name and votes apart */
  }

  .party-name {
    font-weight: 600;
    font-size: 1.1rem;
    color: #000000;
    flex: 1;
  }

  .party-votes {
    font-size: 1rem;
    color: #000000;
    font-weight: bold;
    margin-left: 1rem;
    text-align: right;
    min-width: 100px;
  }
  .party-percentage {
    font-size: 1rem;
    color: #123c98;
    font-weight: bold;
    margin-left: 1rem;
  }


  .party-row:hover {
    background-color: #e0f2fe;
    border-color: #60a5fa;
    transform: scale(1.02);
  }

  .candidate:hover {
    background-color: #efefef;
  }

  .party-list {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem;
    background-color: #f9fafb;
    border-radius: 0.5rem;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  }

  .back-button:hover {
    background-color: #0053ba;
    color: white;
  }

  .filtered-data {
    border: 1px solid black;
  }




/* jouw bestaande CSS hier (uitgeknipt uit de oorspronkelijke component) */
</style>
