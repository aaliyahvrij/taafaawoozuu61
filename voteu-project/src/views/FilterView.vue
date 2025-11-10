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
    getPoStLevel_affiListLhMap(selectedElection.value, selectedPoSt.value.id.toString())
  } else if (selectedElection.value && selectedConsti.value && selectedMuni.value) {
    getMuniLevel_affiListLhMap(selectedElection.value, selectedMuni.value.id.toString())
  } else if (selectedElection.value && selectedConsti.value && !selectedMuni.value) {
    getConstiLevel_affiListLhMap(selectedElection.value, selectedConsti.value.id.toString())
  } else if (selectedElection.value && selectedProvi.value && !selectedConsti.value) {
    getProviLevel_affiList(selectedElection.value, selectedProvi.value.id)
  } else if (selectedElection.value) {
    getNationalLevel_affiListLhMap(selectedElection.value)
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
  muniList.value = []
  selectedPoSt.value = null
  poStList.value = []
}

function clearSelectedPoSt(): void {
  selectedPoSt.value = null
}

async function getNationalLevel_proviListLhMap(electionId: string | null): Promise<void> {
  try {
    if (electionId) {
      const response = await ElectionService.getProviListLhMap(electionId)
      proviList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching proviListLhMap of election', electionId)
    } else {
      proviList.value = []
    }
  } catch (error) {
    console.error('Error fetching proviListLhMap of election', electionId, ': ', error)
  }
}

async function getNationalLevel_affiListLhMap(electionId: string): Promise<void> {
  try {
    const response = await ElectionService.getAffiListLhMap(electionId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'national'
    console.log('Fetching affiListLhMap of election ', electionId)
  } catch (error) {
    console.error('Error fetching affiListLhMap of election ', electionId, ': ', error)
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
  } catch (error) {
    console.error('Error fetching constiList of ', proviPath, ': ', error)
  }
}

async function getProviLevel_affiList(electionId: string, provId: number): Promise<void> {
  const proviPath = 'election ' + electionId + ' > provi ' + provId
  try {
    const response = await ProviService.getAffiList(electionId, provId)
    affiList.value = response
    voteLevel.value = 'provi'
    console.log('Fetching affiList of ', proviPath)
  } catch (error) {
    console.error('Error fetching affiList of ', proviPath, ': ', error)
  }
}

async function getConstiLevel_muniListLhMap(
  electionId: string | null,
  constId: string | undefined,
): Promise<void> {
  const constiPath = 'election ' + electionId + ' > consti ' + constId
  try {
    if (electionId && constId) {
      const response = await ConstiService.getMuniListLhMap(electionId, constId)
      muniList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching muniListLhMap of ', constiPath)
    }
  } catch (error) {
    console.error('Error fetching muniListLhMap of ', constiPath, ': ', error)
  }
}

async function getConstiLevel_affiListLhMap(electionId: string, constId: string): Promise<void> {
  const constiPath = 'election ' + electionId + ' > consti ' + constId
  try {
    const response = await ConstiService.getAffiListLhMap(electionId, constId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'consti'
    console.log('Fetching affiListLhMap of ', constiPath)
  } catch (error) {
    console.error('Error fetching affiListLhMap of ', constiPath, ': ', error)
  }
}

async function getMuniLevel_poStListLhMap(
  electionId: string | null,
  munId: string | undefined,
): Promise<void> {
  const muniPath = 'election ' + electionId + ' > muni ' + munId
  try {
    if (electionId && munId) {
      const response = await MuniService.getPoStListLhMap(electionId, munId)
      poStList.value = Array.isArray(response) ? response : Object.values(response || {})
      console.log('Fetching poStListLhMap of ', muniPath)
    }
  } catch (error) {
    console.error('Error fetching poStListLhMap of ', muniPath, ': ', error)
  }
}

async function getMuniLevel_affiListLhMap(electionId: string, munId: string): Promise<void> {
  const muniPath = 'election ' + electionId + ' > muni ' + munId
  try {
    const response = await MuniService.getAffiListLhMap(electionId, munId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    voteLevel.value = 'muni'
    console.log('Fetching affiListLhMap of ', muniPath)
  } catch (error) {
    console.error('Error fetching affiListLhMap of ', muniPath, ': ', error)
  }
}

async function getPoStLevel_affiListLhMap(electionId: string, poStId: string): Promise<void> {
  const poStPath = 'election ' + electionId + ' > poSt ' + poStId
  try {
    const response = await PoStService.getAffiListLhMap(electionId, poStId)
    affiList.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('votes', affiList.value)
    voteLevel.value = 'poSt'
    console.log('Fetching affiListLhMap of ', poStPath)
  } catch (error) {
    console.error('Error fetching affiListLhMap of ', poStPath, ': ', error)
  }
}

function handleAffiChange(affi: Affiliation): void {
  selectedAffi.value = affi
}

function handleCandiChange(candi: Candidate): void {
  selectedCandi.value = candi
}

function sortCandiListByName(candiList: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandiListByName(candiList)
}

function sortCandiListByVVCount(candiList: Candidate[]): Candidate[] {
  return AffiStyleService.sortCandiListByVVCount(candiList)
}
</script>

<template>
  <div class="filterBar">
    <div class="electionFilter">
      <select
        class="dropdown"
        v-model="selectedElection"
        @change="getNationalLevel_proviListLhMap(selectedElection)"
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
    <div class="proviFilter">
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
    <div class="constiFilter">
      <select
        class="dropdown"
        v-if="constiList.length > 0"
        v-model="selectedConsti"
        @change="getConstiLevel_muniListLhMap(selectedElection, selectedConsti?.id.toString())"
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
    <div class="muniFilter">
      <select
        class="dropdown"
        v-if="muniList.length > 0"
        v-model="selectedMuni"
        @change="getMuniLevel_poStListLhMap(selectedElection, selectedMuni?.id.toString())"
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
    <div class="poSt_filter">
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
      <button v-if="selectedElection" class="applyBtn" @click="handleApply()">Apply filters</button>
    </div>
  </div>
  <div class="filteredData">
    <div class="affiList" v-if="selectedElection && displayedAffiVotes && !selectedAffi">
      <p>{{ voteLevel }} affiliation votes of Election {{ selectedElection }}</p>
      <AffiChart v-if="affiList" :affiList="displayedAffiVotes" />
      <div
        class="affiRow"
        v-for="affi in displayedAffiVotes"
        :key="affi.id"
        @click="handleAffiChange(affi)"
        :style="{ backgroundColor: AffiStyleService.generateColorFromName(affi.name) }"
      >
        <div class="affiName">{{ affi.name }}</div>
        <div class="affiVVCount">{{ affi.vvCount.toLocaleString() }} votes</div>
        <div class="affiPercentage">{{ affi.percentage.toFixed(2) }}%</div>
      </div>
    </div>
    <div v-if="selectedAffi && selectedElection && !selectedCandi">
      <h1 class="affiTitle">{{ selectedAffi.name }}</h1>
      <h2 class="candiListTitle">Candidates</h2>
      <div class="buttons">
        <button class="backBtn" @click="selectedAffi = null">Back</button>
        <button
          class="backBtn"
          @click="selectedAffi.candiList = sortCandiListByName(selectedAffi.candiList)"
        >
          sort by name
        </button>
        <button
          class="backBtn"
          @click="selectedAffi.candiList = sortCandiListByVVCount(selectedAffi.candiList)"
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
    <div v-if="selectedCandi && selectedElection" class="candiDetailCard">
      <h2 class="candiTitle" v-if="selectedCandi.shortCode">
        {{ selectedCandi.shortCode }}
      </h2>
      <h3 class="candiName" v-if="selectedCandi.firstName && selectedCandi.lastName">
        {{ selectedCandi.firstName }} {{ selectedCandi.lastName }}
      </h3>
      <p class="candInfo">
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
      <p class="candiVVCount">
        Votes: <strong>{{ selectedCandi.vvCount.toLocaleString() }}</strong>
      </p>
      <button class="backBtn" @click="selectedCandi = null">Back</button>
    </div>
  </div>
</template>

<style scoped>
.dropdown,
.tag,
.electionFilter select,
.constiFilter select,
.muniFilter select {
  width: 100%;
}

.tag,
.affiTitle,
.candiDetailCard {
  text-align: center;
}

.affiVVCount {
  min-width: 100px;
  text-align: right;
}

.applyBtn
.backBtn {
  border: none;
}

.filteredData {
  border: 1px solid black;
}

.dropdown {
  border: 1px solid #d1d5db;
}

.dropdown,
.applyBtn,
.affiList,
.affiRow,
.candi {
  border-radius: 0.5rem;
}

.dropdown,
.applyBtn {
  padding: 0.5rem;
}

.affiName {
  color: #1f2937;
  flex: 1;
}

.applyBtn,
.affiList p,
.affiName,
.affiVVCount,
.affiPercentage,
.backBtn,
.candInfo {
  font-size: 1rem;
}

.affiTitle,
.candiTitle {
  font-size: 2.5rem;
}

.affiName,
.backBtn {
  font-weight: 600;
}

.affiVVCount,
.affiPercentage {
  font-weight: bold;
}

.tag:hover {
  background-color: #00379a;
}

.backBtn,
.applyBtn:hover,
.backBtn:hover {
  background-color: #0053ba;
}

.candi:hover {
  background-color: #efefef;
}

.affiVVCount,
.candiVVCount {
  color: #000000;
}

.candInfo {
  line-height: 1.5;
  color: #555;
}

.affiPercentage {
  color: #123c98;
}

.candiName {
  font-size: 1.75rem;
  color: #34495e;
}

.tag,
.applyBtn:hover
.backBtn,
.backBtn:hover {
  color: white;
}

.tag,
.affiRow,
.candi,
.filterBar {
  display: flex;
}

.filterBar {
  flex-direction: row;
}

.tag,
.affiRow,
.candi {
  align-items: center;
}

.affiList {
  background-color: #f9fafb;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.tag {
  border-radius: 15px;
  background-color: #002970;
  padding: 2px;
  justify-content: center;
  gap: 10px; /* optional: space between text and icon */
}

.affiList p {
  font-weight: 500;
  color: #333;
  margin: 0;
}

.tag,
.dropdown,
.buttons button {
  margin: 0.5rem;
}

.applyBtn {
  background-color: #66817d;
  color: #ffffff;
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
}

.candiDetailCard {
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  padding: 24px 32px;
  margin: 24px auto;
}

.candiTitle {
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
}

.candiName,
.candInfo {
  margin-bottom: 16px;
}

.candiVVCount {
  font-size: 1.25rem;
  margin-bottom: 24px;
}

.affiVVCount,
.affiPercentage,
.candiListTitle {
  margin-left: 1rem;
}

.electionFilter,
.constiFilter,
.muniFilter {
  min-width: 120px;
  margin-right: 10px;
}

.tag svg,
.tag button:hover,
.backBtn,
.applyBtn {
  cursor: pointer;
}

.affiRow:hover {
  border-color: #60a5fa;
  background-color: #e0f2fe;
  transform: scale(1.02);
}

.backBtn {
  background-color: #002970;
  padding: 10px 24px;
  border-radius: 8px;
  transition: background-color 0.5s ease;
}

.affiRow,
.candi {
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  padding: 0.75rem 1rem;
  justify-content: space-between; /* pushes name and valid vote count apart */
  cursor: pointer;
  transition: all 0.2s ease;
}
</style>
