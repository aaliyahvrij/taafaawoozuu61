<script setup lang="ts">
import { ref } from 'vue'
import type {
  Affiliation,
  Constituency,
  Municipality,
  PollingStation,
  Province,
} from '@/interfaces'
import {
  ConstiService,
  ElectionService,
  MuniService,
  PoStService,
  ProviService,
} from '@/services'

// The first filter set
const selectedElection1 = ref<'TK2021' | 'TK2023' | null>(null)
const selectedProvi1 = ref<Province | null>(null)
const selectedConsti1 = ref<Constituency | null>(null)
const selectedMuni1 = ref<Municipality | null>(null)
const selectedPoSt1 = ref<PollingStation | null>(null)
const affiVotes1 = ref<Affiliation[] | null>(null)
const voteLevel1 = ref<'national' | 'provi' | 'consti' | 'muni' | 'poSt' | null>(
  null,
)
const provinces1 = ref<Province[]>([])
const constituencies1 = ref<Constituency[]>([])
const municipalities1 = ref<Municipality[]>([])
const pollingStations1 = ref<PollingStation[]>([])

// The second filter set
const selectedElection2 = ref<'TK2021' | 'TK2023' | null>(null)
const selectedProvi2 = ref<Province | null>(null)
const selectedConsti2 = ref<Constituency | null>(null)
const selectedMuni2 = ref<Municipality | null>(null)
const selectedPollingStation2 = ref<PollingStation | null>(null)
const affiVotes2 = ref<Affiliation[] | null>(null)
const voteLevel2 = ref<'national' | 'provi' | 'consti' | 'muni' | 'poSt' | null>(
  null,
)

const provinces2 = ref<Province[]>([])
const constituencies2 = ref<Constituency[]>([])
const municipalities2 = ref<Municipality[]>([])
const pollingStations2 = ref<PollingStation[]>([])

// --- Helper functies voor ophalen filters ---
async function getElectoralLevel_provincesOf(
  electionId: string | null,
  provincesRef: typeof provinces1,
  clearSelectedProvince: () => void,
) {
  if (!electionId) {
    provincesRef.value = []
    clearSelectedProvince()
    return
  }
  try {
    const response = await ProviService.getElectoralLevel_provincesOf(electionId)
    provincesRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching provinces of ', electionId)
  } catch (err) {
    console.error('Error fetching provinces of ', electionId, '', err)
  }
}

async function getProviLevel_constituenciesOf(
  electionId: string | null,
  provId: string | undefined,
  constituenciesRef: typeof constituencies1,
  clearSelectedConstituency: () => void,
) {
  if (!electionId || !provId) {
    constituenciesRef.value = []
    clearSelectedConstituency()
    return
  }
  const proviPath = electionId + ' > provi ' + provId
  try {
    const response = await ProviService.getProviLevel_constituenciesOf(electionId, provId)
    constituenciesRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching constituencies of ', proviPath)
  } catch (err) {
    console.error('Error fetching constituencies of ', proviPath, ': ', err)
  }
}

async function getConstiLevel_municipalitiesOf(
  electionId: string | null,
  constId: string | undefined,
  municipalitiesRef: typeof municipalities1,
  clearSelectedMunicipality: () => void,
) {
  if (!electionId || !constId) {
    municipalitiesRef.value = []
    clearSelectedMunicipality()
    return
  }
  const constiPath = electionId + ' > consti ' + constId
  try {
    const response = await MuniService.getConstiLevel_municipalitiesOf(electionId, constId)
    municipalitiesRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching municipalities of ', constiPath)
  } catch (err) {
    console.error('Error fetching municipalities of ', constiPath, ': ', err)
  }
}

async function getMuniLevel_pollingStationsOf(
  electionId: string | null,
  constId: string | undefined,
  munId: string | undefined,
  pollingStationsRef: typeof pollingStations1,
  clearSelectedPollingStation: () => void,
) {
  if (!electionId || !constId || !munId) {
    pollingStationsRef.value = []
    clearSelectedPollingStation()
    return
  }
  const muniPath = electionId + ' > consti ' + constId + ' > muni ' + munId
  try {
    const response = await PoStService.getMuniLevel_pollingStationsOf(
      electionId,
      constId,
      munId,
    )
    pollingStationsRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching polling stations of ', muniPath)
  } catch (err) {
    console.error('Error fetching polling stations of ', muniPath, ': ', err)
  }
}

async function getAffiliationsOf(
  electionId: string | null,
  province: Province | null,
  municipality: Municipality | null,
  constituency: Constituency | null,
  pollingStation: PollingStation | null,
  affiVotesRef: typeof affiVotes1,
  voteLevelRef: typeof voteLevel1,
) {
  if (!electionId) {
    affiVotesRef.value = null
    voteLevelRef.value = null
    return
  }
  let logPath = electionId
  try {
    if (pollingStation && municipality && constituency) {
      const response = await PoStService.getPoStLevel_affiliationsOf(
        electionId,
        constituency.id.toString(),
        municipality.id.toString(),
        pollingStation.id.toString(),
      )
      affiVotesRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'poSt'
      logPath +=
        ' > consti ' +
        constituency.id.toString() +
        ' > muni ' +
        municipality.id.toString() +
        ' > poSt ' +
        pollingStation.id
      console.log('Fetching affiliations of ', logPath)
    } else if (municipality && constituency) {
      const response = await MuniService.getMuniLevel_affiliationsOf(
        electionId,
        constituency.id.toString(),
        municipality.id.toString(),
      )
      affiVotesRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'muni'
      logPath += ' > consti ' + constituency.id.toString() + ' > muni ' + municipality.id.toString()
      console.log('Fetching affiliations of ', logPath)
    } else if (constituency) {
      const response = await ConstiService.getConstiLevel_affiliationsOf(
        electionId,
        constituency.id.toString(),
      )
      affiVotesRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'consti'
      logPath += ' > consti ' + constituency.id
      console.log('Fetching affiliations of ', logPath)
    } else if (province) {
      const response = await ProviService.getProviLevel_affiliationsOf(electionId, province.id)
      affiVotesRef.value = response
      voteLevelRef.value = 'provi'
      logPath += ' > provi ' + province.id
      console.log('Fetching affiliations of ', logPath)
    } else {
      const response = await ElectionService.getElectoralLevel_affiliationsOf(electionId)
      affiVotesRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'national'
      console.log('Fetching affiliations of ', logPath)
    }
  } catch (err) {
    console.error('Error fetching affiliations of ', logPath, ': ', err)
    affiVotesRef.value = null
    voteLevelRef.value = null
  }
}

// --- Reset functies per filterset ---
function clearProviAndBelow1() {
  selectedProvi1.value = null
  constituencies1.value = []
  selectedConsti1.value = null
  municipalities1.value = []
  selectedMuni1.value = null
  pollingStations1.value = []
  selectedPoSt1.value = null
}

function clearConstiAndBelow1() {
  selectedConsti1.value = null
  municipalities1.value = []
  selectedMuni1.value = null
  pollingStations1.value = []
  selectedPoSt1.value = null
}

function clearMuniAndBelow1() {
  selectedMuni1.value = null
  pollingStations1.value = []
  selectedPoSt1.value = null
}

function clearPollingStation1() {
  selectedPoSt1.value = null
}

function clearProviAndBelow2() {
  selectedProvi2.value = null
  constituencies2.value = []
  selectedConsti2.value = null
  municipalities2.value = []
  selectedMuni2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearConstiAndBelow2() {
  selectedConsti2.value = null
  municipalities2.value = []
  selectedMuni2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearMuniAndBelow2() {
  selectedMuni2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearPollingStation2() {
  selectedPollingStation2.value = null
}

// Handles first filter set
async function onElectionChange1() {
  clearProviAndBelow1()
  affiVotes1.value = null
  voteLevel1.value = null
  await getElectoralLevel_provincesOf(selectedElection1.value, provinces1, clearProviAndBelow1)
}

async function onProvinceChange1() {
  clearConstiAndBelow1()
  affiVotes1.value = null
  voteLevel1.value = null
  if (selectedProvi1.value) {
    await getProviLevel_constituenciesOf(
      selectedElection1.value,
      selectedProvi1.value.id.toString(),
      constituencies1,
      clearConstiAndBelow1,
    )
  }
}

async function onConstiChange1() {
  clearMuniAndBelow1()
  affiVotes1.value = null
  voteLevel1.value = null
  if (selectedConsti1.value) {
    await getConstiLevel_municipalitiesOf(
      selectedElection1.value,
      selectedConsti1.value.id.toString(),
      municipalities1,
      clearMuniAndBelow1,
    )
  }
}

async function onMuniChange1() {
  clearPollingStation1()
  affiVotes1.value = null
  voteLevel1.value = null
  if (selectedMuni1.value) {
    await getMuniLevel_pollingStationsOf(
      selectedElection1.value,
      selectedConsti1.value?.id.toString(),
      selectedMuni1.value.id.toString(),
      pollingStations1,
      clearPollingStation1,
    )
  }
}

function onPollingStationChange1() {
  affiVotes1.value = null
  voteLevel1.value = null
}

// Handles second filter set
async function onElectionChange2() {
  clearProviAndBelow2()
  affiVotes2.value = null
  voteLevel2.value = null
  await getElectoralLevel_provincesOf(selectedElection2.value, provinces2, clearProviAndBelow2)
}

async function onProvinceChange2() {
  clearConstiAndBelow2()
  affiVotes2.value = null
  voteLevel2.value = null
  if (selectedProvi2.value) {
    await getProviLevel_constituenciesOf(
      selectedElection2.value,
      selectedProvi2.value.id.toString(),
      constituencies2,
      clearConstiAndBelow2,
    )
  }
}

async function onConstiChange2() {
  clearMuniAndBelow2()
  affiVotes2.value = null
  voteLevel2.value = null
  if (selectedConsti2.value) {
    await getConstiLevel_municipalitiesOf(
      selectedElection2.value,
      selectedConsti2.value.id.toString(),
      municipalities2,
      clearMuniAndBelow2,
    )
  }
}

async function onMuniChange2() {
  clearPollingStation2()
  affiVotes2.value = null
  voteLevel2.value = null
  if (selectedMuni2.value) {
    await getMuniLevel_pollingStationsOf(
      selectedElection2.value,
      selectedConsti2.value?.id.toString(),
      selectedMuni2.value.id.toString(),
      pollingStations2,
      clearPollingStation2,
    )
  }
}

function onPollingStationChange2() {
  affiVotes2.value = voteLevel2.value = null
}

// (both sets)
async function applyFilter() {
  if (!selectedElection1.value && !selectedElection2.value) {
    alert('Select at least one election year to show results.')
    return
  }
  if (selectedElection1.value) {
    await getAffiliationsOf(
      selectedElection1.value,
      selectedProvi1.value,
      selectedMuni1.value,
      selectedConsti1.value,
      selectedPoSt1.value,
      affiVotes1,
      voteLevel1,
    )
  } else {
    affiVotes1.value = voteLevel1.value = null
  }
  if (selectedElection2.value) {
    await getAffiliationsOf(
      selectedElection2.value,
      selectedProvi2.value,
      selectedMuni2.value,
      selectedConsti2.value,
      selectedPollingStation2.value,
      affiVotes2,
      voteLevel2,
    )
  } else {
    affiVotes2.value = null
    voteLevel2.value = null
  }
}
</script>

<template>
  <div class="compare-view">
    <h2 style="text-align: center; margin-bottom: 1rem">Compare Election Results</h2>
    <div class="filters-wrapper">
      <!-- Filter Set 1 -->
      <div class="filter-set">
        <h3>Set 1</h3>
        <select v-model="selectedElection1" @change="onElectionChange1">
          <option value="null" disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>
        <select v-if="provinces1.length > 0" v-model="selectedProvi1" @change="onProvinceChange1">
          <option value="null" disabled>Select province</option>
          <option v-for="provi in provinces1" :key="provi.id" :value="provi">
            {{ provi.name }}
          </option>
        </select>
        <select
          v-if="constituencies1.length > 0"
          v-model="selectedConsti1"
          @change="onConstiChange1"
        >
          <option value="null" disabled>Select constituency</option>
          <option v-for="consti in constituencies1" :key="consti.id" :value="consti">
            {{ consti.name }}
          </option>
        </select>
        <select v-if="municipalities1.length > 0" v-model="selectedMuni1" @change="onMuniChange1">
          <option value="null" disabled>Select municipality</option>
          <option v-for="muni in municipalities1" :key="muni.id" :value="muni">
            {{ muni.name }}
          </option>
        </select>
        <select
          v-if="pollingStations1.length > 0"
          v-model="selectedPoSt1"
          @change="onPollingStationChange1"
        >
          <option value="null" disabled>Select polling station</option>
          <option
            v-for="poSt in pollingStations1"
            :key="poSt.id"
            :value="poSt"
          >
            {{ poSt.name }}
          </option>
        </select>
      </div>

      <!-- Filter Set 2 -->
      <div class="filter-set">
        <h3>Set 2</h3>
        <select v-model="selectedElection2" @change="onElectionChange2">
          <option value="null" disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>
        <select v-if="provinces2.length > 0" v-model="selectedProvi2" @change="onProvinceChange2">
          <option value="null" disabled>Select province</option>
          <option v-for="provi in provinces2" :key="provi.id" :value="provi">
            {{ provi.name }}
          </option>
        </select>
        <select
          v-if="constituencies2.length > 0"
          v-model="selectedConsti2"
          @change="onConstiChange2"
        >
          <option value="null" disabled>Select constituency</option>
          <option v-for="consti in constituencies2" :key="consti.id" :value="consti">
            {{ consti.name }}
          </option>
        </select>
        <select v-if="municipalities2.length > 0" v-model="selectedMuni2" @change="onMuniChange2">
          <option value="null" disabled>Select municipality</option>
          <option v-for="muni in municipalities2" :key="muni.id" :value="muni">
            {{ muni.name }}
          </option>
        </select>
        <select
          v-if="pollingStations2.length > 0"
          v-model="selectedPollingStation2"
          @change="onPollingStationChange2"
        >
          <option value="null" disabled>Select polling station</option>
          <option
            v-for="pollingStation in pollingStations2"
            :key="pollingStation.id"
            :value="pollingStation"
          >
            {{ pollingStation.name }}
          </option>
        </select>
      </div>
    </div>
    <button @click="applyFilter" style="display: block; margin: 1rem auto">Compare</button>
    <div class="results-wrapper">
      <!-- Placeholder texts for results -->
      <div class="result-set">
        <p v-if="affiVotes1">Results for Set 1 (will be replaced by chart)</p>
        <p v-else>No data for Set 1</p>
      </div>
      <div class="result-set">
        <p v-if="affiVotes2">Results for Set 2 (will be replaced by chart)</p>
        <p v-else>No data for Set 2</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.compare-view,
.filter-set {
  padding: 1rem;
}

.filters-wrapper {
  display: flex;
  gap: 2rem;
}

.filter-set {
  min-width: 300px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.filter-set select {
  width: 100%;
  display: block;
  margin-bottom: 1rem;
}
</style>
