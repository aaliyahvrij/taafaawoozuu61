<script setup lang="ts">
import { ref, computed } from 'vue'

import { ConstituencyServiceService } from '@/services/ConstituencyService.ts'
import { AuthorityService } from '@/services/AuthorityService.ts'
import { ElectionService } from '@/services/ElectionService.ts'
import { ProvinceService } from '@/services/ProvinceService.ts'
import { PollingStationService } from '@/services/PollingStationService.ts'
import { PartyStyleService } from '@/services/PartyStyleService.ts'

import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { Party } from '@/interface/Party.ts'
import type { Candidate } from '@/interface/Candidate.ts'
import type { Province } from '@/interface/Province.ts'
import type { PollingStation } from '@/interface/PollingStation.ts'

import YearFilter from '@/components/filters/YearFilter.vue'
import ProvinceFilter from '@/components/filters/ProvinceFilter.vue'
import ConstituencyFilter from '@/components/filters/ConstituencyFilter.vue'
import AuthorityFilter from '@/components/filters/AuthorityFilter.vue'
import PollingStationFilter from '@/components/filters/PollingStationFilter.vue'
import VoteListOverview from '@/components/filters/VoteListOverview.vue'

const selectedElection = ref<'2021' | '2023' | null>(null)
const selectedProvince = ref<Province | null>(null)
const selectedConstituency = ref<Constituency | null>(null)
const selectedAuthority = ref<Authority | null>(null)
const selectedPollingStation = ref<PollingStation | null>(null)

const selectedParty = ref<Party | null>(null)
const partyVotes = ref<Party[] | null>(null)
const selectedCandidate = ref<Candidate | null>(null)

const provinces = ref<Province[]>([])
const constituencies = ref<Constituency[]>([])
const authorities = ref<Authority[]>([])
const pollingStations = ref<PollingStation[]>([])

const hasApplied = ref(false)
const currentVoteLevel = ref<
  'National' | 'Constituency' | 'Municipality' | 'Province' | 'PollingStation' | null
>(null)

const displayedPartyVotes = computed(() => partyVotes.value)

function handleApply(): void {
  hasApplied.value = true
  selectedParty.value = null
  partyVotes.value = null
  selectedCandidate.value = null

  if (
    selectedElection.value &&
    selectedConstituency.value &&
    selectedAuthority.value &&
    selectedPollingStation.value
  ) {
    getPollingStationVotes(
      selectedElection.value,
      selectedConstituency.value.id.toString(),
      selectedAuthority.value.id.toString(),
      selectedPollingStation.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConstituency.value && selectedAuthority.value) {
    getAuthorityPartyVotes(
      selectedElection.value,
      selectedConstituency.value.id.toString(),
      selectedAuthority.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConstituency.value && !selectedAuthority.value) {
    getConstituencyPartyVotes(selectedElection.value, selectedConstituency.value.id.toString())
  } else if (selectedElection.value && selectedProvince.value && !selectedConstituency.value) {
    getProvincePartyVotes(selectedElection.value, selectedProvince.value.id)
  } else if (selectedElection.value) {
    getNationalPartyVotes(selectedElection.value)
  } else {
    console.warn('Invalid selection state.')
  }
}

function clearSelectedElection(): void {
  selectedParty.value = null
  partyVotes.value = null
  hasApplied.value = false
  selectedElection.value = null
  selectedProvince.value = null
  provinces.value = []
  selectedConstituency.value = null
  constituencies.value = []
  selectedAuthority.value = null
  authorities.value = []
  currentVoteLevel.value = null
  selectedPollingStation.value = null
  pollingStations.value = []
}

function clearSelectedProvince(): void {
  selectedProvince.value = null
  constituencies.value = []
  selectedConstituency.value = null
  authorities.value = []
  selectedAuthority.value = null
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedConstituency(): void {
  selectedConstituency.value = null
  selectedAuthority.value = null
  authorities.value = []
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedAuthority(): void {
  selectedAuthority.value = null
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedPollingStation(): void {
  selectedPollingStation.value = null
}

async function getNationalPartyVotes(electionId: string): Promise<void> {
  try {
    console.log('Fetching national party votes for election:', electionId)
    const response = await ElectionService.getNationalPartyVotes(electionId)
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'National'
  } catch (error) {
    console.error('Error fetching national party votes:', error)
  }
}

async function getProvincePartyVotes(electionId: string, provinceId: number): Promise<void> {
  try {
    const response = await ProvinceService.getProvincePartyVotes(electionId, provinceId)
    partyVotes.value = response
    currentVoteLevel.value = 'Province'
  } catch (error) {
    console.error('Error fetching province party votes:', error)
  }
}

async function getConstituencyPartyVotes(
  electionId: string,
  constituencyId: string,
): Promise<void> {
  try {
    console.log(
      'Fetching constituency party votes for election:',
      electionId,
      'constituency:',
      constituencyId,
    )
    const response = await ConstituencyServiceService.getConstituencyPartyVotes(
      electionId,
      constituencyId,
    )
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'Constituency'
  } catch (error) {
    console.error('Error fetching constituency party votes:', error)
  }
}

async function getAuthorityPartyVotes(
  electionId: string,
  constituencyId: string,
  authorityId: string,
): Promise<void> {
  try {
    console.log(
      'Fetching authority party votes for election:',
      electionId,
      'constituency:',
      constituencyId,
      'authority:',
      authorityId,
    )
    const response = await AuthorityService.getAuthorityVotesByConstituencyId(
      electionId,
      constituencyId,
      authorityId,
    )
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'Municipality'
  } catch (error) {
    console.error('Error fetching authority party votes:', error)
  }
}

async function getPollingStationVotes(
  electionId: string,
  constituencyId: string,
  authorityId: string,
  pollingStationId: string,
): Promise<void> {
  try {
    console.log(
      'Fetching polling station votes for election:',
      electionId,
      'constituency:',
      constituencyId,
      'authority:',
      authorityId,
      'polling station:',
      pollingStationId,
    )
    const response = await PollingStationService.getPollingStationVotesByAuthorityId(
      electionId,
      constituencyId,
      authorityId,
      pollingStationId,
    )
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('votes', partyVotes.value)
    currentVoteLevel.value = 'PollingStation'
  } catch (error) {
    console.error('Error fetching polling station votes:', error)
  }
}

async function getProvincesByElection(election: string | null): Promise<void> {
  try {
    if (election) {
      console.log('Fetching provinces for election:', election)
      const response = await ProvinceService.getProvincesByElection(election)
      provinces.value = Array.isArray(response) ? response : Object.values(response || {})
    } else {
      provinces.value = []
    }
  } catch (error) {
    console.error('Error fetching provinces:', error)
  }
}

async function getConstituenciesByProvinceId(
  election: string | null,
  provinceId: string | undefined,
): Promise<void> {
  try {
    if (election && provinceId) {
      console.log('Fetching constituencies for election:', election, 'province:', provinceId)
      const response = await ProvinceService.getConstituenciesByProvinceId(election, provinceId)
      constituencies.value = Array.isArray(response) ? response : Object.values(response || {})
    } else {
      constituencies.value = []
    }
  } catch (error) {
    console.error('Error fetching constituencies by province:', error)
  }
}

async function getAuthoritiesByConstituency(
  electionId: string | null,
  constituencyId: string | undefined,
): Promise<void> {
  try {
    if (electionId && constituencyId) {
      console.log('Fetching authorities for election:', electionId, 'constituency:', constituencyId)
      const response = await AuthorityService.getAuthoritiesByConstituencyId(
        electionId,
        constituencyId,
      )
      console.log(response)
      authorities.value = Array.isArray(response) ? response : Object.values(response || {})
    }
  } catch (error) {
    console.error('Error fetching authorities:', error)
  }
}

async function getPollingStationsByAuthorityId(
  electionId: string | null,
  constituencyId: string | undefined,
  authorityId: string | undefined,
): Promise<void> {
  try {
    if (electionId && constituencyId && authorityId) {
      console.log(
        'Fetching polling stations for election:',
        electionId,
        'constituency:',
        constituencyId,
        'authority:',
        authorityId,
      )
      const response = await PollingStationService.getPollingStationsByAuthorityId(
        electionId,
        constituencyId,
        authorityId,
      )
      pollingStations.value = Array.isArray(response) ? response : Object.values(response || {})
    }
  } catch (error) {
    console.error('Error fetching polling stations:', error)
  }
}

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
  <div class="filter-bar">
      <div class="filter-bar">
        <!-- Election Filter -->
        <YearFilter
          v-model="selectedElection"
          @changed="getProvincesByElection"
          @cleared="clearSelectedElection"
        />
    </div>
    <!-- Province Filter -->
    <ProvinceFilter
      v-model="selectedProvince"
      :options="provinces"
      @changed="province => getConstituenciesByProvinceId(selectedElection, province?.id.toString())"
      @cleared="clearSelectedProvince"
    />
    <!-- Constituency Filter -->
    <ConstituencyFilter
      v-model="selectedConstituency"
      :options="constituencies"
      @changed="constituency => getAuthoritiesByConstituency(selectedElection, constituency?.id.toString())"
      @cleared="clearSelectedConstituency"
    />
    <!-- Authority Filter -->
    <AuthorityFilter
      v-model="selectedAuthority"
      :options="authorities"
      @changed="authority => getPollingStationsByAuthorityId(
    selectedElection,
    selectedConstituency?.id.toString(),
    authority?.id.toString()
  )"
      @cleared="clearSelectedAuthority"
    />

    <PollingStationFilter
      v-model="selectedPollingStation"
      :options="pollingStations"
      @cleared="clearSelectedPollingStation"
    />

    <div>
      <button v-if="selectedElection" class="apply-button" @click="handleApply()">Apply filters</button>
    </div>
  </div>

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
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  padding: 0.5rem;
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
  border: none;
  font-size: 1rem;
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


</style>
