<script setup lang="ts">
import PartyList from '../PartyList.vue'
import CandidateList from '../CandidateList.vue'

import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'
import type { Candidate } from '@/interface/Candidate'

const {
  selectedCandidate,
  selectedElection,
  currentVoteLevel,
  selectedParty,
  displayedPartyVotes,
  partyVotes
} = defineProps<{
  selectedElection: string | number | null
  displayedPartyVotes: PartyVotesDTO[] | null
  partyVotes: PartyVotesDTO[] | null
  selectedParty: PartyVotesDTO | null
  selectedCandidate: Candidate | null
  currentVoteLevel: string | null
}>()

const emit = defineEmits<{
  (e: 'select-party', party: PartyVotesDTO): void
  (e: 'deselect-party'): void
  (e: 'select-candidate', candidate: Candidate): void
  (e: 'deselect-candidate'): void
  (e: 'sort-candidates-by-name'): void
  (e: 'sort-candidates-by-votes'): void
}>()
</script>

<template>
  <div class="filtered-data">
    <PartyList
      v-if="selectedElection && displayedPartyVotes && !selectedParty"
      :selectedElection="selectedElection"
      :partyVotes="displayedPartyVotes"
      :barChartVotes="partyVotes"
      :currentVoteLevel="currentVoteLevel"
      @select-party="party => emit('select-party', party)"
    />

    <CandidateList
      :selected-election="selectedElection"
      v-else-if="selectedParty && !selectedCandidate"
      :selectedParty="selectedParty"
      @select-candidate="candidate => emit('select-candidate', candidate)"
      @deselect-party="() => emit('deselect-party')"
      @sort-candidates-by-name="() => emit('sort-candidates-by-name')"
      @sort-candidates-by-votes="() => emit('sort-candidates-by-votes')"
    />
  </div>
</template>

<style scoped>
/* Add styles if needed */
</style>
