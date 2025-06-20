<script setup lang="ts">
import type { Candidate } from '@/interface/Candidate.ts'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'

defineProps<{
  selectedParty: PartyVotesDTO
  selectedElection: string | number | null
}>()

defineEmits<{
  (e: 'deselect-party'): void
  (e: 'select-candidate', candidate: Candidate): void
  (e: 'sort-candidates-by-name'): void
  (e: 'sort-candidates-by-votes'): void
}>()
</script>

<template>
  <div>
    <h1 class="party-title">{{ selectedParty.name }}</h1>
    <h2 class="candidate-list-title">Candidates</h2>
    <div class="buttons">
      <button class="back-button" @click="$emit('deselect-party')">Back</button>
      <button class="back-button" @click="$emit('sort-candidates-by-name')">Sort by name</button>
      <button class="back-button" @click="$emit('sort-candidates-by-votes')">Sort by votes</button>
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
</template>

<style scoped>
.candidate-list-title {
  margin-left: 1rem;
}

.party-title {
  font-size: 2.5rem;
  text-align: center;
}

.candidate {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 1rem;
  background-color: #f9fafb;
  border-radius: 0.5rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.back-button {
  background-color: #002970;
  border: none;
  color: white;
  padding: 10px 24px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.candidate:hover {
  background-color: #efefef;
}

.back-button:hover {
  background-color: #0053ba;
  color: white;
}
</style>
