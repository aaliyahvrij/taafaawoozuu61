  <script setup lang="ts">
  import PartyList from '../PartyList.vue'


  import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'
  import type { Candidate } from '@/interface/Candidate'

  defineProps<{
    selectedElection: string | number  | null
    displayedPartyVotes: PartyVotesDTO[] | null
    partyVotes: PartyVotesDTO[] | null
    selectedParty: string | null
    selectedCandidate: Candidate | null
    currentVoteLevel: string | null
  }>()

  defineEmits<{
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
        @select-party="$emit('select-party', $event)"
      />
    </div>

  </template>
  <style>

  </style>
