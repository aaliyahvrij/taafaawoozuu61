<script setup lang="ts">
import { ref, computed } from 'vue'
import { AffiChart } from '@/components'
import type {
  Province,
  Constituency,
  Municipality,
  PollingStation,
  Affiliation,
  Candidate,
} from '@/interfaces'
import {
  AffiStyleService,
  ElectionService,
  ProviService,
  ConstiService,
  MuniService,
  PoStService,
} from '@/services'

const selectedElection = ref<'TK2021' | 'TK2023' | null>(null)
const selectedProvi = ref<Province | null>(null)
const proviList = ref<Province[]>([])
const selectedConsti = ref<Constituency | null>(null)
const constiList = ref<Constituency[]>([])
const selectedMuni = ref<Municipality | null>(null)
const muniList = ref<Municipality[]>([])
const selectedPoSt = ref<PollingStation | null>(null)
const poStList = ref<PollingStation[]>([])
const selectedAffi = ref<Affiliation | null>(null)
const affiList = ref<Affiliation[] | null>(null)
const selectedCandi = ref<Candidate | null>(null)
const hasApplied = ref(false)
const voteLevel = ref<'national' | 'provi' | 'consti' | 'muni' | 'poSt' | null>(null)
const displayedAffiVotes = computed(() => affiList.value)

function handleApply(): void {
  hasApplied.value = true
  selectedAffi.value = null
  affiList.value = null
  selectedCandi.value = null
  if (selectedElection.value && selectedConsti.value && selectedMuni.value && selectedPoSt.value) {
    getPoStLevel_affiList_lhMap(
      selectedElection.value,
      selectedConsti.value.id.toString(),
      selectedMuni.value.id.toString(),
      selectedPoSt.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConsti.value && selectedMuni.value) {
    getMuniLevel_affiList_lhMap(
      selectedElection.value,
      selectedConsti.value.id.toString(),
      selectedMuni.value.id.toString(),
    )
  } else if (selectedElection.value && selectedConsti.value && !selectedMuni.value) {
    getConstiLevel_affiList_lhMap(selectedElection.value, selectedConsti.value.id.toString())
  } else if (selectedElection.value && selectedProvi.value && !selectedConsti.value) {
    getProviLevel_affiList(selectedElection.value, selectedProvi.value.id)
  } else if (selectedElection.value) {
    getNationalLevel_affiList_lhMap(selectedElection.value)
  } else {
    console.warn('Invalid selection state.')
  }
}

function clearSelectedElection(): void {
  selectedAffi.value = null
  affiList.value = null
  hasApplied.value = false
  selectedElection.value = null
  selectedProvi.value = null
  proviList.value = []
  selectedConsti.value = null
  constiList.value = []
  selectedMuni.value = null
  muniList.value = []
  voteLevel.value = null
  selectedPoSt.value = null
  poStList.value = []
}

function clearSelectedProvi(): void {
  selectedProvi.value = null
  constiList.value = []
  selectedConsti.value = null
  selectedMuni.value = null
  muniList.value = []
  selectedPoSt.value = null
  poStList.value = []
}

function clearSelectedConsti(): void {
  selectedConsti.value = null
  selectedMuni.value = null
  muniList.value = []
  selectedPoSt.value = null
  poStList.value = []
}

function clearSelectedMuni(): void {
  selectedMuni.value = null
  selectedPoSt.value = null
  poStList.value = []
}

function clearSelectedPoSt(): void {
  selectedPoSt.value = null
}

async function getNationalLevel_proviList_lhMap(electionId: string | null): Promise<void> {
  const proviPath = 'election ' + electionId
  try {
    if (electionId) {
      const response = await ElectionService.getProviList_lhMap(electionId)
      proviList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching proviList_lhMap of ', proviPath)
    } else {
      proviList.value = []
    }
  } catch (err) {
    console.error('Error fetching proviList_lhMap of ', proviPath, ': ', err)
  }
}

async function getNationalLevel_affiList_lhMap(electionId: string): Promise<void> {
  const proviPath = 'election ' + electionId
  try {
    const response = await ElectionService.getAffiList_lhMap(electionId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'national'
    console.log('Fetching affiList_lhMap of ', proviPath)
  } catch (err) {
    console.error('Error fetching affiList_lhMap of ', proviPath, ': ', err)
  }
}

async function getProviLevel_constiList(
  electionId: string | null,
  provId: string | undefined,
): Promise<void> {
  const proviPath = 'election ' + electionId + ' > provi ' + provId
  try {
    if (electionId && provId) {
      const response = await ProviService.getConstiList(electionId, provId)
      constiList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching constiList of ', proviPath)
    } else {
      constiList.value = []
    }
  } catch (err) {
    console.error('Error fetching constiList of ', proviPath, ': ', err)
  }
}

async function getProviLevel_affiList(electionId: string, provId: number): Promise<void> {
  const proviPath = 'election ' + electionId + ' > provi ' + provId
  try {
    const response = await ProviService.getAffiList(electionId, provId)
    affiList.value = response
    voteLevel.value = 'provi'
    console.log('Fetching affiList of ', proviPath)
  } catch (err) {
    console.error('Error fetching affiList of ', proviPath, ': ', err)
  }
}

async function getConstiLevel_muniList_lhMap(
  electionId: string | null,
  constId: string | undefined,
): Promise<void> {
  const constiPath = 'election ' + electionId + ' > consti ' + constId
  try {
    if (electionId && constId) {
      const response = await ConstiService.getMuniList_lhMap(electionId, constId)
      muniList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching muniList_lhMap of ', constiPath)
    }
  } catch (err) {
    console.error('Error fetching muniList_lhMap of', constiPath, ': ', err)
  }
}

async function getConstiLevel_affiList_lhMap(electionId: string, constId: string): Promise<void> {
  const constiPath = 'election ' + electionId + ' > consti ' + constId
  try {
    const response = await ConstiService.getAffiList_lhMap(electionId, constId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'consti'
    console.log('Fetching affiList_lhMap of ', constiPath)
  } catch (err) {
    console.error('Error fetching affiList_lhMap of ', constiPath, ': ', err)
  }
}

async function getMuniLevel_poStList_lhMap(
  electionId: string | null,
  constId: string | undefined,
  munId: string | undefined,
): Promise<void> {
  const muniPath = 'election ' + electionId + ' > consti ' + constId + ' > muni ' + munId
  try {
    if (electionId && constId && munId) {
      const response = await MuniService.getPoStList_lhMap(electionId, constId, munId)
      poStList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching poStList_lhMap of ', muniPath)
    }
  } catch (err) {
    console.error('Error fetching poStList_lhMap of ', muniPath, ': ', err)
  }
}

async function getMuniLevel_affiList_lhMap(
  electionId: string,
  constId: string,
  munId: string,
): Promise<void> {
  const muniPath = 'election ' + electionId + ' > consti ' + constId + ' > muni ' + munId
  try {
    const response = await MuniService.getAffiList_lhMap(electionId, constId, munId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'muni'
    console.log('Fetching affiList_lhMap of ', muniPath)
  } catch (err) {
    console.error('Error fetching affiList_lhMap of ', muniPath, ': ', err)
  }
}

async function getPoStLevel_affiList_lhMap(
  electionId: string,
  constId: string,
  munId: string,
  poStId: string,
): Promise<void> {
  const poStPath =
    'election ' + electionId + ' > consti ' + constId + ' > muni ' + munId + ' > poSt ' + poStId
  try {
    const response = await PoStService.getAffiList_lhMap(electionId, constId, munId, poStId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('votes', affiList.value)
    voteLevel.value = 'poSt'
    console.log('Fetching affiList_lhMap of ', poStPath)
  } catch (err) {
    console.error('Error fetching affiList_lhMap of ', poStPath, ': ', err)
  }
}

function handleAffiChange(affi: Affiliation): void {
  selectedAffi.value = affi
}

function handleCandiChange(candi: Candidate): void {
  selectedCandi.value = candi
}

function sortCandidatesByName(candiList: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandidatesByName(candiList)
}

function sortCandidatesByVVCount(candiList: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandidatesByVVCount(candiList)
}
</script>

<template>
  <div class="filter-bar">
    <div class="election-filter">
      <select
        class="dropdown"
        v-model="selectedElection"
        @change="getNationalLevel_proviList_lhMap(selectedElection)"
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
        v-if="proviList.length > 0"
        v-model="selectedProvi"
        @change="getProviLevel_constiList(selectedElection, selectedProvi?.id.toString())"
      >
        <option value="null" disabled>Select a province</option>
        <option v-for="provi in proviList" :key="provi.id" :value="provi">
          {{ provi.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedProvi">
        {{ selectedProvi.name }}
        <svg
          @click="clearSelectedProvi()"
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
        v-if="constiList.length > 0"
        v-model="selectedConsti"
        @change="getConstiLevel_muniList_lhMap(selectedElection, selectedConsti?.id.toString())"
      >
        <option value="null" disabled>Select a constituency</option>
        <option v-for="consti in constiList" :key="consti.id" :value="consti">
          {{ consti.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedConsti">
        {{ selectedConsti.name }}
        <svg
          @click="clearSelectedConsti()"
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
        v-if="muniList.length > 0"
        v-model="selectedMuni"
        @change="
          getMuniLevel_poStList_lhMap(
            selectedElection,
            selectedConsti?.id.toString(),
            selectedMuni?.id.toString(),
          )
        "
      >
        <option value="null" disabled>Select a municipality</option>
        <option v-for="muni in muniList" :key="muni.id" :value="muni">
          {{ muni.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedMuni">
        {{ selectedMuni.name }}
        <svg
          @click="clearSelectedMuni()"
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
    <div class="po-st-filter">
      <select class="dropdown" v-if="poStList.length > 0" v-model="selectedPoSt">
        <option value="null" disabled>Select a polling station</option>
        <option v-for="poSt in poStList" :key="poSt.id" :value="poSt">
          {{ poSt.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedPoSt">
        {{ selectedPoSt.name }}
        <svg
          @click="clearSelectedPoSt()"
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
    <div class="affi-list" v-if="selectedElection && displayedAffiVotes && !selectedAffi">
      <p>{{ voteLevel }} affiliation votes of Election {{ selectedElection }}</p>
      <AffiChart v-if="affiList" :affiVotes="displayedAffiVotes" />
      <div
        class="affi-row"
        v-for="affi in displayedAffiVotes"
        :key="affi.id"
        @click="handleAffiChange(affi)"
        :style="{ backgroundColor: AffiStyleService.generateColorFromName(affi.name) }"
      >
        <div class="affi-name">{{ affi.name }}</div>
        <div class="affi-vv-count">{{ affi.vvCount.toLocaleString() }} votes</div>
        <div class="affi-percentage">{{ affi.percentage.toFixed(2) }}%</div>
      </div>
    </div>
    <div v-if="selectedAffi && selectedElection && !selectedCandi">
      <h1 class="affi-title">{{ selectedAffi.name }}</h1>
      <h2 class="candi-list-title">Candidates</h2>
      <div class="buttons">
        <button class="back-btn" @click="selectedAffi = null">Back</button>
        <button
          class="back-btn"
          @click="selectedAffi.candiList = sortCandidatesByName(selectedAffi.candiList)"
        >
          sort by name
        </button>
        <button
          class="back-btn"
          @click="selectedAffi.candiList = sortCandidatesByVVCount(selectedAffi.candiList)"
        >
          sort by votes
        </button>
      </div>
      <div
        class="candi"
        v-for="candi in selectedAffi.candiList"
        :key="candi.id"
        @click="handleCandiChange(candi)"
      >
        <p v-if="candi.firstName && candi.lastName">
          {{ candi.firstName }} {{ candi.lastName }} : {{ candi.vvCount.toLocaleString() }} votes
        </p>
      </div>
    </div>
    <div v-if="selectedCandi && selectedElection" class="candi-details-card">
      <h2 class="candi-title" v-if="selectedCandi.shortCode">
        {{ selectedCandi.shortCode }}
      </h2>
      <h3 class="candi-name" v-if="selectedCandi.firstName && selectedCandi.lastName">
        {{ selectedCandi.firstName }} {{ selectedCandi.lastName }}
      </h3>
      <p class="candi-info">
        <strong>Gender:</strong> {{ selectedCandi.gender }}
        <svg
          v-if="selectedCandi.gender === 'male'"
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
          v-if="selectedCandi.gender === 'female'"
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
        <strong>Locality:</strong> {{ selectedCandi.localityName }}
      </p>
      <p class="candi-vv-count">
        Votes: <strong>{{ selectedCandi.vvCount.toLocaleString() }}</strong>
      </p>
      <button class="back-btn" @click="selectedCandi = null">Back</button>
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

.candi-vv-count {
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

.candi:hover {
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
.candi {
  padding: 0.75rem 1rem;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 0.375rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  justify-content: space-between; /* pushes name and valid vote count apart */
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

.affi-vv-count,
.candi-vv-count {
  color: #000000;
}

.affi-vv-count {
  min-width: 100px;
  text-align: right;
}

.affi-percentage {
  color: #123c98;
}

.affi-vv-count,
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

.affi-vv-count,
.affi-percentage {
  font-weight: bold;
}

.affi-vv-count,
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
.candi,
.filter-bar {
  display: flex;
}

.filter-bar {
  flex-direction: row;
}

.tag,
.affi-row,
.candi {
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
