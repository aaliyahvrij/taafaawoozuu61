<script setup lang="ts">
import type { Candidate } from '@/interface/Candidate.ts'

const selectedCandidate = defineModel<Candidate | null>('selectedCandidate')
</script>

<template>
  <div v-if="selectedCandidate">
    <h1 v-if="selectedCandidate.shortCode">{{ selectedCandidate.shortCode }}</h1>
    <h1 v-if="selectedCandidate.firstName && selectedCandidate.lastName">
      {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
    </h1>
    <h4>Gender: {{ selectedCandidate.gender }}</h4>
    <h4>Locality: {{ selectedCandidate.locality }}</h4>
    <h1>Votes: {{ selectedCandidate.votes.toLocaleString() }}</h1>
    <button @click="selectedCandidate = null">Back</button>
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

