<script setup lang="ts">
import PartyList from '../PartyList.vue'
import CandidateList from '@/components/CandidateList.vue'
import CandidateDetails from '@/components/CandidateDetails.vue'

import type { Party } from '@/interface/Party'
import type { Candidate } from '@/interface/Candidate'

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
    <PartyList
      v-if="selectedElection && displayedPartyVotes && !selectedParty"
      :selectedElection="selectedElection"
      :partyVotes="displayedPartyVotes"
      :barChartVotes="partyVotes"
      :currentVoteLevel="currentVoteLevel"
      @select-party="$emit('select-party', $event)"
    />

    <CandidateList
      v-else-if="selectedParty && !selectedCandidate"
      :selectedParty="selectedParty"
      @select-candidate="$emit('select-candidate', $event)"
      @deselect-party="$emit('deselect-party')"
      @sort-candidates-by-name="$emit('sort-candidates-by-name')"
      @sort-candidates-by-votes="$emit('sort-candidates-by-votes')"
    />

    <CandidateDetails
      v-else-if="selectedCandidate"
      :candidate="selectedCandidate"
      @deselect-candidate="$emit('deselect-candidate')"
    />
  </div>
</template>
