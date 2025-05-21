<script setup lang="ts">

import { ref, computed } from 'vue'

import { ConstituencyServiceService } from '@/services/ConstituencyService.ts'
import { AuthorityService } from '@/services/AuthorityService.ts'
import { ElectionService } from '@/services/ElectionService.ts'
import { ProvinceService } from '@/services/ProvinceService.ts'
import { PollingStationService } from '@/services/PollingStationService.ts'

import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { Party } from '@/interface/Party.ts'
import type { Candidate } from '@/interface/Candidate.ts'
import type { Province } from '@/interface/Province.ts'
import type {PollingStation} from '@/interface/PollingStation.ts'

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
const currentVoteLevel = ref<'national' | 'constituency' | 'municipality' | 'province' | 'pollingStation' |  null>(null)

const displayedPartyVotes = computed(() => partyVotes.value)

function handleApply(): void {
  hasApplied.value = true
  selectedParty.value = null
  partyVotes.value = null
  selectedCandidate.value = null

  if (selectedElection.value && selectedConstituency.value && selectedAuthority.value && selectedPollingStation.value) {
    getPollingStationVotes(selectedElection.value, selectedConstituency.value.id.toString(), selectedAuthority.value.id.toString(), selectedPollingStation.value.id.toString())

  } else if (selectedElection.value && selectedConstituency.value && selectedAuthority.value) {
    getAuthorityPartyVotes(selectedElection.value, selectedConstituency.value.id.toString(), selectedAuthority.value.id.toString())

  } else if (selectedElection.value && selectedConstituency.value && !selectedAuthority.value) {
    getConstituencyPartyVotes(selectedElection.value, selectedConstituency.value.id.toString())

  } else if (selectedElection.value && selectedProvince.value && !selectedConstituency.value) {
    getProvincePartyVotes(selectedElection.value, selectedProvince.value.id)

  } else if (selectedElection.value) {
    getNationalPartyVotes(selectedElection.value)

  } else {
    console.warn("Invalid selection state.")
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
    currentVoteLevel.value = 'national'
  } catch (error) {
    console.error("Error fetching national party votes:", error)
  }
}

async function getProvincePartyVotes(electionId: string, provinceId: number): Promise<void> {
  try {
    const response = await ProvinceService.getProvincePartyVotes(electionId, provinceId)
    partyVotes.value = response
    currentVoteLevel.value = 'province'
  } catch (error) {
    console.error("Error fetching province party votes:", error)
  }
}

async function getConstituencyPartyVotes(electionId: string, constituencyId: string): Promise<void> {
  try {
    console.log('Fetching constituency party votes for election:', electionId, 'constituency:', constituencyId)
    const response = await ConstituencyServiceService.getConstituencyPartyVotes(electionId, constituencyId)
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'constituency'
  } catch (error) {
    console.error("Error fetching constituency party votes:", error)
  }
}

async function getAuthorityPartyVotes(electionId: string, constituencyId: string, authorityId: string): Promise<void> {
  try {
    console.log('Fetching authority party votes for election:', electionId, 'constituency:', constituencyId, 'authority:', authorityId)
    const response = await AuthorityService.getAuthorityVotesByConstituencyId(electionId, constituencyId, authorityId)
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'municipality'
  } catch (error) {
    console.error("Error fetching authority party votes:", error)
  }
}

async function getPollingStationVotes(electionId: string, constituencyId: string, authorityId: string, pollingStationId: string): Promise<void> {
  try {
    console.log('Fetching polling station votes for election:', electionId, 'constituency:', constituencyId, 'authority:', authorityId, 'polling station:', pollingStationId)
    const response = await PollingStationService.getPollingStationVotesByAuthorityId(electionId, constituencyId, authorityId, pollingStationId)
    partyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log("votes", partyVotes.value)
    currentVoteLevel.value = 'pollingStation'
  } catch (error) {
    console.error("Error fetching polling station votes:", error)
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
    console.error("Error fetching provinces:", error)
  }
}


async function getConstituenciesByProvinceId(election: string | null, provinceId: string | undefined): Promise<void> {
  try {
    if (election && provinceId) {
      console.log('Fetching constituencies for election:', election, 'province:', provinceId)
      const response = await ProvinceService.getConstituenciesByProvinceId(election, provinceId)
      constituencies.value = Array.isArray(response) ? response : Object.values(response || {})
    } else {
      constituencies.value = []
    }
  } catch (error) {
    console.error("Error fetching constituencies by province:", error)
  }
}

async function getAuthoritiesByConstituency(electionId: string | null, constituencyId: string | undefined): Promise<void> {
  try {
    if (electionId && constituencyId) {
      console.log('Fetching authorities for election:', electionId, 'constituency:', constituencyId)
      const response = await AuthorityService.getAuthoritiesByConstituencyId(electionId, constituencyId)
      console.log(response)
      authorities.value = Array.isArray(response) ? response : Object.values(response || {})
    }
  } catch (error) {
    console.error("Error fetching authorities:", error)
  }
}

async function getPollingStationsByAuthorityId(electionId: string | null, constituencyId: string | undefined, authorityId: string | undefined): Promise<void> {
  try {
    if (electionId && constituencyId && authorityId) {
      console.log('Fetching polling stations for election:', electionId, 'constituency:', constituencyId, 'authority:', authorityId)
      const response = await PollingStationService.getPollingStationsByAuthorityId(electionId, constituencyId, authorityId)
      pollingStations.value = Array.isArray(response) ? response : Object.values(response || {})
    }
  } catch (error) {
    console.error("Error fetching polling stations:", error)
  }
}


function handlePartyChange(party: Party): void {
  selectedParty.value = party
}

function handleCandidateChange(candidate: Candidate): void {
  selectedCandidate.value = candidate
}
</script>

<template>
  <div class="filter-bar">
    <div class="election-filter">
      <select v-model="selectedElection" @change="getProvincesByElection(selectedElection)">

        <option value=null disabled>Select an election</option>
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
      <select v-if="provinces.length > 0" v-model="selectedProvince" @change="getConstituenciesByProvinceId(selectedElection, selectedProvince?.id.toString())">
        <option value=null disabled>Select a province</option>
        <option v-for="province in provinces" :key="province.id" :value="province">
          {{ province.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedProvince">
        {{ selectedProvince.name }}
        <svg @click="clearSelectedProvince()" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF">
          <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
        </svg>
      </div>
    </div>

    <div class="constituency-filter">
      <select v-if="constituencies.length > 0" v-model="selectedConstituency" @change="getAuthoritiesByConstituency(selectedElection, selectedConstituency?.id.toString())">
        <option value=null disabled>Select a constituency</option>
        <option v-for="constituency in constituencies" :key="constituency.id" :value="constituency">
          {{ constituency.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedConstituency">
        {{ selectedConstituency.name }}
        <svg @click="clearSelectedConstituency()" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF">
          <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
        </svg>
      </div>
    </div>


    <div class="authority-filter">
      <select v-if="authorities.length > 0" v-model="selectedAuthority" @change="getPollingStationsByAuthorityId(selectedElection, selectedConstituency?.id.toString(), selectedAuthority?.id.toString())">
        <option value=null disabled>Select a municipality</option>
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
      <select v-if="pollingStations.length > 0" v-model="selectedPollingStation">
        <option value=null disabled>Select a polling station</option>
        <option v-for="pollingStation in pollingStations" :key="pollingStation.id" :value="pollingStation">
          {{pollingStation.zipCode }} {{ pollingStation.name }}
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
      <button @click="handleApply()">Apply</button>
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
      >
        {{ party.name }}: <b>{{ party.votes.toLocaleString() }}</b> ({{ party.percentage.toFixed(2) }}%)
      </div>
    </div>

    <div v-if="selectedParty && selectedElection && !selectedCandidate">
      <h1>{{ selectedParty.name }}</h1>
      <button @click="selectedParty = null">Back</button>
      <div
        class="candidate"
        v-for="candidate in selectedParty.candidates"
        :key="candidate.id"
        @click="handleCandidateChange(candidate)"
      >
        <p v-if="candidate.shortCode">
          {{ candidate.shortCode }} : {{ candidate.votes.toLocaleString() }}
        </p>
        <p v-if="candidate.firstName && candidate.lastName">
          {{ candidate.firstName }} {{ candidate.lastName }} :
          {{ candidate.votes.toLocaleString() }}
        </p>
      </div>
    </div>

    <div v-if="selectedCandidate && selectedElection">
      <h1 v-if="selectedCandidate.shortCode">{{ selectedCandidate.shortCode }}</h1>
      <h1 v-if="selectedCandidate.firstName && selectedCandidate.lastName">
        {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
        Gender: {{ selectedCandidate.gender }}
        Locality: {{ selectedCandidate.localityName }}
      </h1>
      <h1>Votes: {{ selectedCandidate.votes.toLocaleString() }}</h1>
      <button @click="selectedCandidate = null">Back</button>
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
