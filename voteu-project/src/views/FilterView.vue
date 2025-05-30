<script setup lang="ts">
import { ref, computed } from 'vue'

import { ElectionService } from '@/services/ElectionService.ts'
import { ProviService } from '@/services/ProviService.ts'
import { ConstiService } from '@/services/ConstiService.ts'
import { MuniService } from '@/services/MuniService.ts'
import { PollingStationService } from '@/services/PollingStationService.ts'
import { AffiStyleService } from '@/services/AffiStyleService.ts'
import AffiChart from '@/components/Data/charts/AffiChart.vue'
import type { Province } from '@/interfaces/Province.ts'
import type { Constituency } from '@/interfaces/Constituency.ts'
import type { Municipality } from '@/interfaces/Municipality.ts'
import type { Affiliation } from '@/interfaces/Affiliation.ts'
import type { PollingStation } from '@/interfaces/PollingStation.ts'
import type { Candidate } from '@/interfaces/Candidate.ts'

const selectedElection = ref<'2021' | '2023' | null>(null)
const selectedProvince = ref<Province | null>(null)
const provinces = ref<Province[]>([])
const selectedConstituency = ref<Constituency | null>(null)
const constituencies = ref<Constituency[]>([])
const selectedMunicipality = ref<Municipality | null>(null)
const municipalities = ref<Municipality[]>([])
const selectedPollingStation = ref<PollingStation | null>(null)
const pollingStations = ref<PollingStation[]>([])
const selectedAffiliation = ref<Affiliation | null>(null)
const affiVotes = ref<Affiliation[] | null>(null)
const selectedCandidate = ref<Candidate | null>(null)
const hasApplied = ref(false)
const currentVoteLevel = ref<
  'national' | 'provincial' | 'constituencial' | 'municipal' | 'pollingStation' | null
>(null)
const displayedAffiVotes = computed(() => affiVotes.value)

function handleApply(): void {
  hasApplied.value = true
  selectedAffiliation.value = null
  affiVotes.value = null
  selectedCandidate.value = null
  if (
    selectedElection.value &&
    selectedConstituency.value &&
    selectedMunicipality.value &&
    selectedPollingStation.value
  ) {
    getMuniLevel_PollingStationVotes(
      selectedElection.value,
      selectedConstituency.value.id.toString(),
      selectedMunicipality.value.id.toString(),
      selectedPollingStation.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConstituency.value && selectedMunicipality.value) {
    getMuniLevel_affiVotes(
      selectedElection.value,
      selectedConstituency.value.id.toString(),
      selectedMunicipality.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConstituency.value && !selectedMunicipality.value) {
    getConstiLevel_affiVotes(selectedElection.value, selectedConstituency.value.id.toString())
  } else if (selectedElection.value && selectedProvince.value && !selectedConstituency.value) {
    getProviLevel_affiVotes(selectedElection.value, selectedProvince.value.id)
  } else if (selectedElection.value) {
    getNationalLevel_affiVotes(selectedElection.value)
  } else {
    console.warn('Invalid selection state.')
  }
}

function clearSelectedElection(): void {
  selectedAffiliation.value = null
  affiVotes.value = null
  hasApplied.value = false
  selectedElection.value = null
  selectedProvince.value = null
  provinces.value = []
  selectedConstituency.value = null
  constituencies.value = []
  selectedMunicipality.value = null
  municipalities.value = []
  currentVoteLevel.value = null
  selectedPollingStation.value = null
  pollingStations.value = []
}

function clearSelectedProvince(): void {
  selectedProvince.value = null
  constituencies.value = []
  selectedConstituency.value = null
  municipalities.value = []
  selectedMunicipality.value = null
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedConstituency(): void {
  selectedConstituency.value = null
  selectedMunicipality.value = null
  municipalities.value = []
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedMunicipality(): void {
  selectedMunicipality.value = null
  pollingStations.value = []
  selectedPollingStation.value = null
}

function clearSelectedPollingStation(): void {
  selectedPollingStation.value = null
}

async function getElectoralLevel_provinces(election: string | null): Promise<void> {
  try {
    if (election) {
      const response = await ProviService.getElectoralLevel_provinces(election)
      provinces.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching electoral level provinces for election ', election)
    } else {
      provinces.value = []
    }
  } catch (error) {
    console.error('Error fetching electoral level provinces: ', error)
  }
}

async function getProviLevel_constituencies(
  election: string | null,
  provId: string | undefined,
): Promise<void> {
  try {
    if (election && provId) {
      const response = await ProviService.getProviLevel_constituencies(election, provId)
      constituencies.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log(
        'Fetching provincial level constituencies for election ',
        election,
        'province ',
        provId,
      )
    } else {
      constituencies.value = []
    }
  } catch (error) {
    console.error('Error fetching provincial level constituencies: ', error)
  }
}

async function getConstiLevel_municipalities(
  electionId: string | null,
  constId: string | undefined,
): Promise<void> {
  try {
    if (electionId && constId) {
      const response = await MuniService.getConstiLevel_municipalities(electionId, constId)
      municipalities.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log(
        'Fetching constituencial level municipalities for election ',
        electionId,
        'constituency ',
        constId,
      )
    }
  } catch (error) {
    console.error('Error fetching consituencial level municipalities: ', error)
  }
}

async function getMuniLevel_pollingStations(
  electionId: string | null,
  constId: string | undefined,
  munId: string | undefined,
): Promise<void> {
  try {
    if (electionId && constId && munId) {
      const response = await PollingStationService.getMuniLevel_pollingStations(
        electionId,
        constId,
        munId,
      )
      pollingStations.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log(
        'Fetching municipal level polling stations for election ',
        electionId,
        'constituency ',
        constId,
        'municipality ',
        munId,
      )
    }
  } catch (error) {
    console.error('Error fetching municipal level polling stations: ', error)
  }
}

async function getMuniLevel_PollingStationVotes(
  electionId: string,
  constId: string,
  munId: string,
  pollingStationId: string,
): Promise<void> {
  try {
    const response = await PollingStationService.getMuniLevel_pollingStationVotes(
      electionId,
      constId,
      munId,
      pollingStationId,
    )
    affiVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('votes', affiVotes.value)
    currentVoteLevel.value = 'pollingStation'
    console.log(
      'Fetching municipal level polling station votes for election ',
      electionId,
      'constituency ',
      constId,
      'municipality ',
      munId,
      'polling station ',
      pollingStationId,
    )
  } catch (error) {
    console.error('Error fetching municipal level polling station votes: ', error)
  }
}

async function getNationalLevel_affiVotes(electionId: string): Promise<void> {
  try {
    const response = await ElectionService.getAffiVotes(electionId)
    affiVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'national'
    console.log('Fetching national level affiliation votes for election ', electionId)
  } catch (error) {
    console.error('Error fetching national level affiliation votes: ', error)
  }
}

async function getProviLevel_affiVotes(electionId: string, provId: number): Promise<void> {
  try {
    const response = await ProviService.getAffiVotes(electionId, provId)
    affiVotes.value = response
    currentVoteLevel.value = 'provincial'
    console.log('Fetching provincial level affiliation votes for election ', electionId)
  } catch (error) {
    console.error('Error fetching provincial level affiliation votes: ', error)
  }
}

async function getConstiLevel_affiVotes(electionId: string, constId: string): Promise<void> {
  try {
    const response = await ConstiService.getAffiVotes(electionId, constId)
    affiVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'constituencial'
    console.log(
      'Fetching constituency ' + constId + ' level affiliation votes for election ',
      electionId,
    )
  } catch (error) {
    console.error('Error fetching constituencial level affiliation votes:', error)
  }
}

async function getMuniLevel_affiVotes(
  electionId: string,
  constId: string,
  munId: string,
): Promise<void> {
  try {
    const response = await MuniService.getConstiLevel_muniVotes(electionId, constId, munId)
    affiVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    currentVoteLevel.value = 'municipal'
    console.log(
      'Fetching municipal level affiliation votes for election ',
      electionId,
      'constituency ',
      constId,
      'municipality ',
      munId,
    )
  } catch (error) {
    console.error('Error fetching municipal level affiliation votes: ', error)
  }
}

function handleAffiChange(affiliation: Affiliation): void {
  selectedAffiliation.value = affiliation
}

function handleCandiChange(candidate: Candidate): void {
  selectedCandidate.value = candidate
}

function sortCandidatesByName(candidates: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandidatesByName(candidates)
}

function sortCandidatesByVotes(candidates: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandidatesByVotes(candidates)
}
</script>

<template>
  <div class="filter-bar">
    <div class="election-filter">
      <select
        class="dropdown"
        v-model="selectedElection"
        @change="getElectoralLevel_provinces(selectedElection)"
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
    <div class="provi-filter">
      <select
        class="dropdown"
        v-if="provinces.length > 0"
        v-model="selectedProvince"
        @change="getProviLevel_constituencies(selectedElection, selectedProvince?.id.toString())"
      >
        <option value="null" disabled>Select a province</option>
        <option v-for="provi in provinces" :key="provi.id" :value="provi">
          {{ provi.name }}
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
    <div class="consti-filter">
      <select
        class="dropdown"
        v-if="constituencies.length > 0"
        v-model="selectedConstituency"
        @change="
          getConstiLevel_municipalities(selectedElection, selectedConstituency?.id.toString())
        "
      >
        <option value="null" disabled>Select a constituency</option>
        <option v-for="consti in constituencies" :key="consti.id" :value="consti">
          {{ consti.name }}
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
    <div class="muni-filter">
      <select
        class="dropdown"
        v-if="municipalities.length > 0"
        v-model="selectedMunicipality"
        @change="
          getMuniLevel_pollingStations(
            selectedElection,
            selectedConstituency?.id.toString(),
            selectedMunicipality?.id.toString(),
          )
        "
      >
        <option value="null" disabled>Select a municipality</option>
        <option v-for="muni in municipalities" :key="muni.id" :value="muni">
          {{ muni.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedMunicipality">
        {{ selectedMunicipality.name }}
        <svg
          @click="clearSelectedMunicipality()"
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
      <button v-if="selectedElection" class="apply-btn" @click="handleApply()">
        Apply filters
      </button>
    </div>
  </div>
  <div class="filtered-data">
    <div class="affi-list" v-if="selectedElection && displayedAffiVotes && !selectedAffiliation">
      <p>{{ currentVoteLevel }} affiliation votes for Election {{ selectedElection }}</p>
      <AffiChart v-if="affiVotes" :affiVotes="displayedAffiVotes" />
      <div
        class="affi-row"
        v-for="affi in displayedAffiVotes"
        :key="affi.id"
        @click="handleAffiChange(affi)"
        :style="{ backgroundColor: AffiStyleService.generateColorFromName(affi.name) }"
      >
        <div class="affi-name">{{ affi.name }}</div>
        <div class="affi-votes">{{ affi.votes.toLocaleString() }} votes</div>
        <div class="affi-percentage">{{ affi.percentage.toFixed(2) }}%</div>
      </div>
    </div>
    <div v-if="selectedAffiliation && selectedElection && !selectedCandidate">
      <h1 class="affi-title">{{ selectedAffiliation.name }}</h1>
      <h2 class="candi-list-title">Candidates</h2>
      <div class="buttons">
        <button class="back-btn" @click="selectedAffiliation = null">Back</button>
        <button
          class="back-btn"
          @click="
            selectedAffiliation.candidates = sortCandidatesByName(selectedAffiliation.candidates)
          "
        >
          sort by name
        </button>
        <button
          class="back-btn"
          @click="
            selectedAffiliation.candidates = sortCandidatesByVotes(selectedAffiliation.candidates)
          "
        >
          sort by votes
        </button>
      </div>
      <div
        class="candidate"
        v-for="candi in selectedAffiliation.candidates"
        :key="candi.id"
        @click="handleCandiChange(candi)"
      >
        <p v-if="candi.firstName && candi.lastName">
          {{ candi.firstName }} {{ candi.lastName }} : {{ candi.votes.toLocaleString() }} votes
        </p>
      </div>
    </div>
    <div v-if="selectedCandidate && selectedElection" class="candi-details-card">
      <h2 class="candi-title" v-if="selectedCandidate.shortCode">
        {{ selectedCandidate.shortCode }}
      </h2>
      <h3 class="candi-name" v-if="selectedCandidate.firstName && selectedCandidate.lastName">
        {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
      </h3>
      <p class="candi-info">
        <strong>Gender:</strong> {{ selectedCandidate.gender }}
        <svg
          v-if="selectedCandidate.gender === 'male'"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#2854C5"
        >
          <path
            d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"
          />
        </svg>
        <svg
          v-if="selectedCandidate.gender === 'female'"
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#B87E9F"
        >
          <path
            d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"
          />
        </svg>
        <br />
        <strong>Locality:</strong> {{ selectedCandidate.localityName }}
      </p>
      <p class="candi-votes">
        Votes: <strong>{{ selectedCandidate.votes.toLocaleString() }}</strong>
      </p>
      <button class="back-btn" @click="selectedCandidate = null">Back</button>
    </div>
  </div>
</template>
<style scoped>
.candi-details-card {
  background: #f9fafb;
  border-radius: 12px;
  padding: 24px 32px;
  margin: 24px auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.tag,
.affi-title,
.candi-details-card {
  text-align: center;
}

.candi-title {
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
}

.candi-name {
  font-size: 1.75rem;
  color: #34495e;
}

.candi-info {
  color: #555;
  line-height: 1.5;
}

.candi-name,
.candi-info {
  margin-bottom: 16px;
}

.candi-votes {
  font-size: 1.25rem;
  margin-bottom: 24px;
}

.back-btn {
  background-color: #002970;
  padding: 10px 24px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.apply-btn {
  background-color: #66817d;
  color: #ffffff;
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
}

.back-btn,
.apply-btn {
  border: none;
}

.tag,
.back-btn,
.back-btn:hover,
.apply-btn:hover {
  color: white;
}

.back-btn,
.back-btn:hover,
.apply-btn:hover {
  background-color: #0053ba;
}

.dropdown {
  border: 1px solid #d1d5db;
}

.apply-btn,
.dropdown {
  border-radius: 0.375rem;
  padding: 0.5rem;
}

.candidate:hover {
  background-color: #efefef;
}

.affi-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding: 1rem;
  background-color: #f9fafb;
  border-radius: 0.5rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.affi-list p {
  margin: 0;
  font-weight: 500;
  color: #333;
}

.affi-row,
.candidate {
  padding: 0.75rem 1rem;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 0.375rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  justify-content: space-between; /* pushes name and votes apart */
  transition: all 0.2s ease;
}

.affi-name {
  color: #1f2937;
  flex: 1;
}

.affi-name,
.back-btn {
  font-weight: 600;
}

.affi-votes,
.candi-votes {
  color: #000000;
}

.affi-votes {
  min-width: 100px;
  text-align: right;
}

.affi-percentage {
  color: #123c98;
}

.affi-votes,
.affi-percentage,
.back-btn,
.apply-btn {
  font-size: 1rem;
}

.affi-list p,
.affi-name,
.candi-info {
  font-size: 1.1rem;
}

.affi-title,
.candi-title {
  font-size: 2.5rem;
}

.affi-votes,
.affi-percentage {
  font-weight: bold;
}

.affi-votes,
.affi-percentage,
.candi-list-title {
  margin-left: 1rem;
}

.affi-row:hover {
  background-color: #e0f2fe;
  border-color: #60a5fa;
  transform: scale(1.02);
}

.filtered-data {
  border: 1px solid black;
}

.election-filter,
.consti-filter,
.muni-filter {
  min-width: 120px;
  margin-right: 10px;
}

.dropdown,
.tag,
.election-filter select,
.consti-filter select,
.muni-filter select {
  width: 100%;
}

.tag {
  padding: 2px;
  justify-content: center;
  gap: 10px; /* optional: space between text and icon */
  background-color: #002970;
  border-radius: 15px;
}

.tag,
.dropdown,
.buttons button {
  margin: 0.5rem;
}

.tag,
.affi-row,
.candidate,
.filter-bar {
  display: flex;
}

.filter-bar {
  flex-direction: row;
}

.tag,
.affi-row,
.candidate {
  align-items: center;
}

.tag svg,
.tag button:hover,
.back-btn,
.apply-btn {
  cursor: pointer;
}

.tag:hover {
  background-color: #00379a;
}
</style>
