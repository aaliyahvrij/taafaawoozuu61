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

import PartyChart from '@/components/Data/charts/PartyChart.vue'
//mport BarPartyChart from '@/components/Data/charts/Bar/BarPartyChart.vue'

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
function sortCandidateNames(candidates: Candidate[]): Candidate[] {
  return PartyStyleService.sortCandidateNames(candidates)
}
function sortCandidateVotes(candidates: Candidate[]): Candidate[] {
  return PartyStyleService.sortCandidatesByVotes(candidates)
}
</script>

<template>
  <div class="filter-bar">
    <div class="election-filter">
      <select
        class="dropdown"
        v-model="selectedElection"
        @change="getProvincesByElection(selectedElection)"
      >
        <option value="null" disabled>Select an election</option>
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
      <div class="tag" v-if="selectedElection">
        {{ selectedElection }}
        <svg
          @click="clearSelectedElection()"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#FFFFFF"
        >
          <path
            d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
          />
        </svg>
      </div>
    </div>

    <!-- Province Filter -->
    <div class="province-filter">
      <select
        class="dropdown"
        v-if="provinces.length > 0"
        v-model="selectedProvince"
        @change="getConstituenciesByProvinceId(selectedElection, selectedProvince?.id.toString())"
      >
        <option value="null" disabled>Select a province</option>
        <option v-for="province in provinces" :key="province.id" :value="province">
          {{ province.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedProvince">
        {{ selectedProvince.name }}
        <svg
          @click="clearSelectedProvince()"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#FFFFFF"
        >
          <path
            d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
          />
        </svg>
      </div>
    </div>

    <div class="constituency-filter">
      <select
        class="dropdown"
        v-if="constituencies.length > 0"
        v-model="selectedConstituency"
        @change="
          getAuthoritiesByConstituency(selectedElection, selectedConstituency?.id.toString())
        "
      >
        <option value="null" disabled>Select a constituency</option>
        <option v-for="constituency in constituencies" :key="constituency.id" :value="constituency">
          {{ constituency.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedConstituency">
        {{ selectedConstituency.name }}
        <svg
          @click="clearSelectedConstituency()"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#FFFFFF"
        >
          <path
            d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
          />
        </svg>
      </div>
    </div>

    <div class="authority-filter">
      <select
        class="dropdown"
        v-if="authorities.length > 0"
        v-model="selectedAuthority"
        @change="
          getPollingStationsByAuthorityId(
            selectedElection,
            selectedConstituency?.id.toString(),
            selectedAuthority?.id.toString(),
          )
        "
      >
        <option value="null" disabled>Select a municipality</option>
        <option v-for="authority in authorities" :key="authority.id" :value="authority">
          {{ authority.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedAuthority">
        {{ selectedAuthority.name }}
        <svg
          @click="clearSelectedAuthority()"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#FFFFFF"
        >
          <path
            d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
          />
        </svg>
      </div>
    </div>

    <div class="polling-station-filter">
      <select class="dropdown" v-if="pollingStations.length > 0" v-model="selectedPollingStation">
        <option value="null" disabled>Select a polling station</option>
        <option
          v-for="pollingStation in pollingStations"
          :key="pollingStation.id"
          :value="pollingStation"
        >
         {{ pollingStation.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedPollingStation">
        {{ selectedPollingStation.name }}
        <svg
          @click="clearSelectedPollingStation()"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#FFFFFF"
        >
          <path
            d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
          />
        </svg>
      </div>
    </div>

    <div>
      <button v-if="selectedElection" class="apply-button" @click="handleApply()">Apply filters</button>
    </div>
  </div>

  <div class="filtered-data">
    <div class="party-list" v-if="selectedElection && displayedPartyVotes && !selectedParty">
      <p>{{ currentVoteLevel }} party votes for Election {{ selectedElection }}</p>


        <PartyChart v-if="partyVotes" :partyVotes="displayedPartyVotes" />
      <div
        class="party-row"
        v-for="party in displayedPartyVotes"
        :key="party.id"
        @click="handlePartyChange(party)"
        :style="{ backgroundColor: PartyStyleService.generateColorFromName(party.name) }"
      >
        <div class="party-name">{{ party.name }}</div>
        <div class="party-votes">{{ party.votes.toLocaleString() }} votes</div>
        <div class="party-percentage">{{party.percentage.toFixed(2)}} %</div>
      </div>
    </div>

    <div v-if="selectedParty && selectedElection && !selectedCandidate">
      <h1 class="party-title">{{ selectedParty.name }}</h1>
      <h2 class="candidate-list-title">Candidates</h2>
      <div class="buttons">
        <button class="back-button" @click="selectedParty = null">Back</button>
        <button class="back-button" @click="selectedParty.candidates =  sortCandidateNames(selectedParty.candidates)">sort by name</button>
        <button class="back-button" @click="selectedParty.candidates =  sortCandidateVotes(selectedParty.candidates)">sort by votes</button>
      </div>
      <div
        class="candidate"
        v-for="candidate in selectedParty.candidates"
        :key="candidate.id"
        @click="handleCandidateChange(candidate)"
      >
        <p v-if="candidate.firstName && candidate.lastName">
          {{ candidate.firstName }} {{ candidate.lastName }} :
          {{ candidate.votes.toLocaleString() }} votes
        </p>
      </div>
    </div>

    <div v-if="selectedCandidate && selectedElection" class="candidate-details-card">
      <h2 class="candidate-title" v-if="selectedCandidate.shortCode">{{ selectedCandidate.shortCode }}</h2>
      <h3 class="candidate-name" v-if="selectedCandidate.firstName && selectedCandidate.lastName">
        {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
      </h3>
      <p class="candidate-info">
        <strong>Gender:</strong> {{ selectedCandidate.gender }}
        <svg  v-if="selectedCandidate.gender==='male'" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#2854C5"><path d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"/></svg>
        <svg v-if="selectedCandidate.gender==='female'" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#B87E9F"><path d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"/></svg>
        <br />
        <strong>Locality:</strong> {{ selectedCandidate.localityName }}
      </p>
      <p class="candidate-votes">
        Votes: <strong>{{ selectedCandidate.votes.toLocaleString() }}</strong>
      </p>
      <button class="back-button" @click="selectedCandidate = null">Back</button>
    </div>
  </div>
</template>
<style scoped>
.candidate-list-title {
  margin-left: 1rem;
}
.buttons button {
  margin: 0.5rem;
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
  border: none;
  color: white;
  padding: 10px 24px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
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
.apply-button:hover, .back-button:hover {
  background-color: #0053ba;
  color: white;
}

.dropdown {
  width: 100%;
  padding: 0.5rem;
  margin: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
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

.party-list p {
  margin: 0;
  font-weight: 500;
  font-size: 1.1rem;
  color: #333;
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
  color: #1f2937;
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


.filtered-data {
  border: 1px solid black;
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
  margin: 0.5rem;
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
