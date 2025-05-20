<script setup lang="ts">
import type { Candidate } from '@/interface/Candidate.ts'
import type { Party } from '@/interface/Party.ts'

const selectedElection = defineModel<string>('selectedElection')
const selectedParty = defineModel<Party | null>('selectedParty')
const selectedCandidate = defineModel<Candidate | null>('selectedCandidate')

function handleCandidateChange(candidate: Candidate) {
  selectedCandidate.value = candidate
}
</script>

<template>
  <div v-if="selectedParty && selectedElection && !selectedCandidate">
    <h1>{{ selectedParty.name }}</h1>
    <button @click="selectedParty = null">Back</button>
    <div
      class="candidate"
      v-for="candidate in selectedParty.candidates"
      :key="candidate.id"
      @click="handleCandidateChange(candidate)"
    >
      <p v-if="candidate.shortCode">{{ candidate.shortCode }} : {{ candidate.votes.toLocaleString() }}</p>
      <p v-if="candidate.firstName && candidate.lastName">
        {{ candidate.firstName }} {{ candidate.lastName }} : {{ candidate.votes.toLocaleString() }}
      </p>
    </div>
  </div>
</template>
<style scoped>
.candidate {
  border: 1px solid black;
  height: 40px;
}
.candidate:hover {
  background-color: #efefef;
}
.party-list p {
  padding: 20px;
}

.party-row {
  display: flex;
  align-items: center;
  border: 1px solid black;
  height: 40px;
  padding-left: 10px;
}
.party-row:hover {
  background-color: #efefef;
}

.filtered-data {
  border: 1px solid black;
  height: auto;
}
.filter-bar {
  display: flex;
  flex-direction: row;
}

.election-filter,
.constituency-filter,
.authority-filter {
  min-width: 120px;
  margin-right: 10px;
}

.election-filter select,
.constituency-filter select,
.authority-filter select {
  width: 100%;
}

.tag {
  padding: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px; /* optional: space between text and icon */
  background-color: #002970;
  color: white;
  text-align: center;
  border-radius: 15px;
  width: 100%;
  margin-top: 10px;
}

.tag svg {
  cursor: pointer;
}

.tag:hover {
  background-color: #00379a;
}

.tag button:hover {
  cursor: pointer;
}
</style>
