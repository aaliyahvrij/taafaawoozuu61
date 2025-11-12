<script setup lang="ts">
import { ref } from 'vue'
import type {
  Province,
  Constituency,
  Municipality,
  PollingStation,
  Affiliation,
} from '@/interfaces'
import { ConstiService, ElectionService, MuniService, PoStService, ProviService } from '@/services'

// The first filter set
const selectedElection1 = ref<'TK2021' | 'TK2023' | null>(null)
const selectedProvi1 = ref<Province | null>(null)
const selectedConsti1 = ref<Constituency | null>(null)
const selectedMuni1 = ref<Municipality | null>(null)
const selectedPoSt1 = ref<PollingStation | null>(null)
const voteLevel1 = ref<'national' | 'provi' | 'consti' | 'muni' | 'poSt' | null>(null)
const proviList1 = ref<Province[]>([])
const constiList1 = ref<Constituency[]>([])
const muniList1 = ref<Municipality[]>([])
const poStList1 = ref<PollingStation[]>([])
const affiList1 = ref<Affiliation[] | null>(null)

// The second filter set
const selectedElection2 = ref<'TK2021' | 'TK2023' | null>(null)
const selectedProvi2 = ref<Province | null>(null)
const selectedConsti2 = ref<Constituency | null>(null)
const selectedMuni2 = ref<Municipality | null>(null)
const selectedPoSt2 = ref<PollingStation | null>(null)
const voteLevel2 = ref<'national' | 'provi' | 'consti' | 'muni' | 'poSt' | null>(null)
const proviList2 = ref<Province[]>([])
const constiList2 = ref<Constituency[]>([])
const muniList2 = ref<Municipality[]>([])
const poStList2 = ref<PollingStation[]>([])
const affiList2 = ref<Affiliation[] | null>(null)

// --- Helper functies voor ophalen filters ---
async function getNationalLevel_proviListLhMap(
  electionId: string | null,
  proviListRef: typeof proviList1,
  clearSelectedProvi: () => void,
) {
  if (!electionId) {
    proviListRef.value = []
    clearSelectedProvi()
    return
  }
  try {
    const response = await ElectionService.getProviListLhMap(electionId)
    proviListRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching proviListLhMap of election ', electionId)
  } catch (error) {
    console.error('Error fetching proviListLhMap of election ', electionId, ': ', error)
  }
}

async function getProviLevel_constiList(
  electionId: string | null,
  provId: string | undefined,
  constiListRef: typeof constiList1,
  clearSelectedConsti: () => void,
) {
  if (!electionId || !provId) {
    constiListRef.value = []
    clearSelectedConsti()
    return
  }
  const proviPath = 'election ' + electionId + ' > provi ' + provId
  try {
    const response = await ProviService.getConstiList(electionId, provId)
    constiListRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching constiList of ', proviPath)
  } catch (error) {
    console.error('Error fetching constiList of ', proviPath, ': ', error)
  }
}

async function getConstiLevel_muniListLhMap(
  electionId: string | null,
  constId: string | undefined,
  muniListRef: typeof muniList1,
  clearSelectedMuni: () => void,
) {
  if (!electionId || !constId) {
    muniListRef.value = []
    clearSelectedMuni()
    return
  }
  const constiPath = 'election ' + electionId + ' > consti ' + constId
  try {
    const response = await ConstiService.getMuniListLhMap(electionId, constId)
    muniListRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching muniListLhMap of ', constiPath)
  } catch (error) {
    console.error('Error fetching muniListLhMap of ', constiPath, ': ', error)
  }
}

async function getMuniLevel_poStListLhMap(
  electionId: string | null,
  munId: string | undefined,
  poStListRef: typeof poStList1,
  clearSelectedPoSt: () => void,
) {
  if (!electionId || !munId) {
    poStListRef.value = []
    clearSelectedPoSt()
    return
  }
  const muniPath = 'election ' + electionId + ' > muni ' + munId
  try {
    const response = await MuniService.getPoStListLhMap(electionId, munId)
    poStListRef.value = Array.isArray(response) ? response : Object.values(response || {})
    console.log('Fetching poStListLhMap of ', muniPath)
  } catch (error) {
    console.error('Error fetching poStListLhMap of ', muniPath, ': ', error)
  }
}

async function getAffiListOrListLhMap(
  electionId: string | null,
  provi: Province | null,
  muni: Municipality | null,
  consti: Constituency | null,
  poSt: PollingStation | null,
  affiListRef: typeof affiList1,
  voteLevelRef: typeof voteLevel1,
) {
  if (!electionId) {
    affiListRef.value = voteLevelRef.value = null
    return
  }
  let levelPath = 'election ' + electionId
  try {
    if (poSt && muni && consti) {
      const response = await PoStService.getAffiListLhMap(electionId, poSt.id.toString())
      affiListRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'poSt'
      levelPath +=
        ' > consti ' + consti.id.toString() + ' > muni ' + muni.id.toString() + ' > poSt ' + poSt.id
      console.log('Fetching affiListLhMap of ', levelPath)
    } else if (muni && consti) {
      const response = await MuniService.getAffiListLhMap(electionId, muni.id.toString())
      affiListRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'muni'
      levelPath += ' > consti ' + consti.id.toString() + ' > muni ' + muni.id.toString()
      console.log('Fetching affiListLhMap of ', levelPath)
    } else if (consti) {
      const response = await ConstiService.getAffiListLhMap(electionId, consti.id.toString())
      affiListRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'consti'
      levelPath += ' > consti ' + consti.id
      console.log('Fetching affiListLhMap of ', levelPath)
    } else if (provi) {
      const response = await ProviService.getAffiList(electionId, provi.id)
      affiListRef.value = response
      voteLevelRef.value = 'provi'
      levelPath += ' > provi ' + provi.id
      console.log('Fetching affiList of ', levelPath)
    } else {
      const response = await ElectionService.getAffiListLhMap(electionId)
      affiListRef.value = Array.isArray(response) ? response : Object.values(response || {})
      voteLevelRef.value = 'national'
      console.log('Fetching affiListLhMap of ', levelPath)
    }
  } catch (err) {
    console.error('Error fetching affiList(LhMap) of ', levelPath, ': ', err)
    affiListRef.value = voteLevelRef.value = null
  }
}

// --- Reset functies per filterset ---
function clearProviAndBelow1() {
  selectedProvi1.value = null
  constiList1.value = []
  selectedConsti1.value = null
  muniList1.value = []
  selectedMuni1.value = null
  poStList1.value = []
  selectedPoSt1.value = null
}

function clearConstiAndBelow1() {
  selectedConsti1.value = null
  muniList1.value = []
  selectedMuni1.value = null
  poStList1.value = []
  selectedPoSt1.value = null
}

function clearMuniAndBelow1() {
  selectedMuni1.value = null
  poStList1.value = []
  selectedPoSt1.value = null
}

function clearPoSt1() {
  selectedPoSt1.value = null
}

function clearProviAndBelow2() {
  selectedProvi2.value = null
  constiList2.value = []
  selectedConsti2.value = null
  muniList2.value = []
  selectedMuni2.value = null
  poStList2.value = []
  selectedPoSt2.value = null
}

function clearConstiAndBelow2() {
  selectedConsti2.value = null
  muniList2.value = []
  selectedMuni2.value = null
  poStList2.value = []
  selectedPoSt2.value = null
}

function clearMuniAndBelow2() {
  selectedMuni2.value = null
  poStList2.value = []
  selectedPoSt2.value = null
}

function clearPoSt2() {
  selectedPoSt2.value = null
}

// Handles first filter set
async function onElectionChange1() {
  clearProviAndBelow1()
  affiList1.value = voteLevel1.value = null
  await getNationalLevel_proviListLhMap(selectedElection1.value, proviList1, clearProviAndBelow1)
}

async function onProviChange1() {
  clearConstiAndBelow1()
  affiList1.value = voteLevel1.value = null
  if (selectedProvi1.value) {
    await getProviLevel_constiList(
      selectedElection1.value,
      selectedProvi1.value.id.toString(),
      constiList1,
      clearConstiAndBelow1,
    )
  }
}

async function onConstiChange1() {
  clearMuniAndBelow1()
  affiList1.value = voteLevel1.value = null
  if (selectedConsti1.value) {
    await getConstiLevel_muniListLhMap(
      selectedElection1.value,
      selectedConsti1.value.id.toString(),
      muniList1,
      clearMuniAndBelow1,
    )
  }
}

async function onMuniChange1() {
  clearPoSt1()
  affiList1.value = voteLevel1.value = null
  if (selectedMuni1.value) {
    await getMuniLevel_poStListLhMap(
      selectedElection1.value,
      selectedMuni1.value.id.toString(),
      poStList1,
      clearPoSt1,
    )
  }
}

function onPoStChange1() {
  affiList1.value = voteLevel1.value = null
}

// Handles second filter set
async function onElectionChange2() {
  clearProviAndBelow2()
  affiList2.value = voteLevel2.value = null
  await getNationalLevel_proviListLhMap(selectedElection2.value, proviList2, clearProviAndBelow2)
}

async function onProviChange2() {
  clearConstiAndBelow2()
  affiList2.value = voteLevel2.value = null
  if (selectedProvi2.value) {
    await getProviLevel_constiList(
      selectedElection2.value,
      selectedProvi2.value.id.toString(),
      constiList2,
      clearConstiAndBelow2,
    )
  }
}

async function onConstiChange2() {
  clearMuniAndBelow2()
  affiList2.value = voteLevel2.value = null
  if (selectedConsti2.value) {
    await getConstiLevel_muniListLhMap(
      selectedElection2.value,
      selectedConsti2.value.id.toString(),
      muniList2,
      clearMuniAndBelow2,
    )
  }
}

async function onMuniChange2() {
  clearPoSt2()
  affiList2.value = voteLevel2.value = null
  if (selectedMuni2.value) {
    await getMuniLevel_poStListLhMap(
      selectedElection2.value,
      selectedMuni2.value.id.toString(),
      poStList2,
      clearPoSt2,
    )
  }
}

function onPoStChange2() {
  affiList2.value = voteLevel2.value = null
}

// (both sets)
async function applyFilter() {
  if (!selectedElection1.value && !selectedElection2.value) {
    alert('Select at least one election year to show results.')
    return
  }
  if (selectedElection1.value) {
    await getAffiListOrListLhMap(
      selectedElection1.value,
      selectedProvi1.value,
      selectedMuni1.value,
      selectedConsti1.value,
      selectedPoSt1.value,
      affiList1,
      voteLevel1,
    )
  } else {
    affiList1.value = voteLevel1.value = null
  }
  if (selectedElection2.value) {
    await getAffiListOrListLhMap(
      selectedElection2.value,
      selectedProvi2.value,
      selectedMuni2.value,
      selectedConsti2.value,
      selectedPoSt2.value,
      affiList2,
      voteLevel2,
    )
  } else {
    affiList2.value = voteLevel2.value = null
  }
}
</script>

<template>
  <div class="compareView">
    <h2 style="text-align: center; margin-bottom: 1rem">Compare Election Results</h2>
    <div class="filterWrapper">
      <!-- Filter Set 1 -->
      <div class="filterSet">
        <h3>Set 1</h3>
        <select v-model="selectedElection1" @change="onElectionChange1">
          <option value="null" disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>
        <select v-if="proviList1.length > 0" v-model="selectedProvi1" @change="onProviChange1">
          <option value="null" disabled>Select province</option>
          <option v-for="provi in proviList1" :key="provi.id" :value="provi">
            {{ provi.name }}
          </option>
        </select>
        <select v-if="constiList1.length > 0" v-model="selectedConsti1" @change="onConstiChange1">
          <option value="null" disabled>Select constituency</option>
          <option v-for="consti in constiList1" :key="consti.id" :value="consti">
            {{ consti.name }}
          </option>
        </select>
        <select v-if="muniList1.length > 0" v-model="selectedMuni1" @change="onMuniChange1">
          <option value="null" disabled>Select municipality</option>
          <option v-for="muni in muniList1" :key="muni.id" :value="muni">
            {{ muni.name }}
          </option>
        </select>
        <select v-if="poStList1.length > 0" v-model="selectedPoSt1" @change="onPoStChange1">
          <option value="null" disabled>Select polling station</option>
          <option v-for="poSt in poStList1" :key="poSt.id" :value="poSt">
            {{ poSt.name }}
          </option>
        </select>
      </div>

      <!-- Filter Set 2 -->
      <div class="filterSet">
        <h3>Set 2</h3>
        <select v-model="selectedElection2" @change="onElectionChange2">
          <option value="null" disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>
        <select v-if="proviList2.length > 0" v-model="selectedProvi2" @change="onProviChange2">
          <option value="null" disabled>Select province</option>
          <option v-for="provi in proviList2" :key="provi.id" :value="provi">
            {{ provi.name }}
          </option>
        </select>
        <select v-if="constiList2.length > 0" v-model="selectedConsti2" @change="onConstiChange2">
          <option value="null" disabled>Select constituency</option>
          <option v-for="consti in constiList2" :key="consti.id" :value="consti">
            {{ consti.name }}
          </option>
        </select>
        <select v-if="muniList2.length > 0" v-model="selectedMuni2" @change="onMuniChange2">
          <option value="null" disabled>Select municipality</option>
          <option v-for="muni in muniList2" :key="muni.id" :value="muni">
            {{ muni.name }}
          </option>
        </select>
        <select v-if="poStList2.length > 0" v-model="selectedPoSt2" @change="onPoStChange2">
          <option value="null" disabled>Select polling station</option>
          <option v-for="poSt in poStList2" :key="poSt.id" :value="poSt">
            {{ poSt.name }}
          </option>
        </select>
      </div>
    </div>
    <button @click="applyFilter" style="display: block; margin: 1rem auto">Compare</button>
    <div class="resultWrapper">
      <!-- Placeholder texts for results -->
      <div class="resultSet">
        <p v-if="affiList1">Results for Set 1 (will be replaced by chart)</p>
        <p v-else>No data for Set 1</p>
      </div>
      <div class="resultSet">
        <p v-if="affiList2">Results for Set 2 (will be replaced by chart)</p>
        <p v-else>No data for Set 2</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.compareView,
.filterSet {
  padding: 1rem;
}

.filterSet {
  min-width: 300px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.filterWrapper {
  display: flex;
  gap: 2rem;
}

.filterSet select {
  width: 100%;
  display: block;
  margin-bottom: 1rem;
}
</style>
