<script setup lang="ts">

import { PartyStyleService } from '@/services/PartyStyleService.ts'


import type { Party } from '@/interface/Party.ts'
import type { Candidate } from '@/interface/Candidate.ts'

import VoteListOverview from '@/components/filters/VoteListOverview.vue'

let filterSelection: number[] = []



function handlePartyChange(party: Party): void {
  selectedParty.value = party
}

function handleCandidateChange(candidate: Candidate): void {
  selectedCandidate.value = candidate
}
function sortByName() {
  if (selectedParty.value)
    selectedParty.value.candidates = PartyStyleService.sortCandidateNames(selectedParty.value.candidates)
}
function sortByVotes() {
  if (selectedParty.value)
    selectedParty.value.candidates = PartyStyleService.sortCandidatesByVotes(selectedParty.value.candidates)
}
</script>

<template>


  <VoteListOverview
    :selectedElection="selectedElection"
    :displayedPartyVotes="displayedPartyVotes"
    :partyVotes="partyVotes"
    :selectedParty="selectedParty"
    :selectedCandidate="selectedCandidate"
    :currentVoteLevel="currentVoteLevel"
    @select-party="handlePartyChange"
    @deselect-party="selectedParty = null"
    @select-candidate="handleCandidateChange"
    @deselect-candidate="selectedCandidate = null"
    @sort-candidates-by-name="sortByName"
    @sort-candidates-by-votes="sortByVotes"
  />
</template>

<style scoped>

.buttons button {
  margin: 0.5rem;
}

.apply-button {
  background-color: #66817d;
  color: #ffffff;
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  padding: 0.5rem;
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  font-size: 1rem;
}

.apply-button:hover {
  background-color: #0053ba;
  color: white;
}

.party-list p {
  margin: 0;
  font-weight: 500;
  font-size: 1.1rem;
  color: #333;
}

.filter-bar {
  display: flex;
  flex-direction: row;
}

.election-filter select,
.constituency-filter select,
.authority-filter select {
  width: 100%;
}

.tag svg {
  cursor: pointer;
}

.tag button:hover {
  cursor: pointer;
}
</style>
