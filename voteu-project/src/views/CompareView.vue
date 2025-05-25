<script setup lang="ts">
import { ref, watch } from 'vue'

import { ConstituencyServiceService } from '@/services/ConstiService.ts'
import { AuthorityService } from '@/services/AuthorityService.ts'
import { ElectionService } from '@/services/ElectionService.ts'
import { ProvinceService } from '@/services/ProvinceService.ts'
import { RepUnitService } from '@/services/RepUnitService.ts'

import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { Party } from '@/interface/Affiliation.ts'
import type { Province } from '@/interface/Province.ts'
import type { RepUnit } from '@/interface/RepUnit.ts'


// the first filter set
const selectedElection1 = ref<'2021' | '2023' | null>(null)
const selectedProvince1 = ref<Province | null>(null)
const selectedConstituency1 = ref<Constituency | null>(null)
const selectedAuthority1 = ref<Authority | null>(null)
const selectedPollingStation1 = ref<RepUnit | null>(null)

const partyVotes1 = ref<Party[] | null>(null)
const currentVoteLevel1 = ref<'national' | 'province' | 'constituency' | 'municipality' | 'pollingStation' | null>(null)

const provinces1 = ref<Province[]>([])
const constituencies1 = ref<Constituency[]>([])
const authorities1 = ref<Authority[]>([])
const pollingStations1 = ref<RepUnit[]>([])

// the second filter set
const selectedElection2 = ref<'2021' | '2023' | null>(null)
const selectedProvince2 = ref<Province | null>(null)
const selectedConstituency2 = ref<Constituency | null>(null)
const selectedAuthority2 = ref<Authority | null>(null)
const selectedPollingStation2 = ref<RepUnit | null>(null)

const partyVotes2 = ref<Party[] | null>(null)
const currentVoteLevel2 = ref<'national' | 'province' | 'constituency' | 'municipality' | 'pollingStation' | null>(null)

const provinces2 = ref<Province[]>([])
const constituencies2 = ref<Constituency[]>([])
const authorities2 = ref<Authority[]>([])
const pollingStations2 = ref<RepUnit[]>([])

// --- Helper functies voor ophalen filters (herbruikbaar) ---
async function getProvincesByElection(election: string | null, provincesRef: typeof provinces1, clearSelectedProvince: () => void) {
  if (!election) {
    provincesRef.value = []
    clearSelectedProvince()
    return
  }
  try {
    const response = await ProvinceService.getProvincesByElection(election)
    provincesRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching provinces:', error)
  }
}

async function getConstituenciesByProvinceId(election: string | null, provinceId: string | undefined, constituenciesRef: typeof constituencies1, clearSelectedConstituency: () => void) {
  if (!election || !provinceId) {
    constituenciesRef.value = []
    clearSelectedConstituency()
    return
  }
  try {
    const response = await ProvinceService.getConstituenciesByProvinceId(election, provinceId)
    constituenciesRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching constituencies:', error)
  }
}

async function getAuthoritiesByConstituency(election: string | null, constituencyId: string | undefined, authoritiesRef: typeof authorities1, clearSelectedAuthority: () => void) {
  if (!election || !constituencyId) {
    authoritiesRef.value = []
    clearSelectedAuthority()
    return
  }
  try {
    const response = await AuthorityService.getAuthoritiesByConstituencyId(election, constituencyId)
    authoritiesRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching authorities:', error)
  }
}

async function getPollingStationsByAuthorityId(election: string | null, constituencyId: string | undefined, authorityId: string | undefined, pollingStationsRef: typeof pollingStations1, clearSelectedPollingStation: () => void) {
  if (!election || !constituencyId || !authorityId) {
    pollingStationsRef.value = []
    clearSelectedPollingStation()
    return
  }
  try {
    const response = await RepUnitService.getPollingStationsByAuthorityId(election, constituencyId, authorityId)
    pollingStationsRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching polling stations:', error)
  }
}

// --- Party votes ophalen (herbruikbaar) ---
async function fetchPartyVotes(
  election: string | null,
  pollingStation: RepUnit | null,
  authority: Authority | null,
  constituency: Constituency | null,
  province: Province | null,
  partyVotesRef: typeof partyVotes1,
  currentVoteLevelRef: typeof currentVoteLevel1
) {
  if (!election) {
    partyVotesRef.value = null
    currentVoteLevelRef.value = null
    return
  }

  try {
    if (pollingStation && authority && constituency) {
      const res = await RepUnitService.getPollingStationVotesByAuthorityId(
        election,
        constituency.id.toString(),
        authority.id.toString(),
        pollingStation.id.toString()
      )
      partyVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'pollingStation'

    } else if (authority && constituency) {
      const res = await AuthorityService.getAuthorityVotesByConstituencyId(
        election,
        constituency.id.toString(),
        authority.id.toString()
      )
      partyVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'municipality'

    } else if (constituency) {
      const res = await ConstituencyServiceService.getConstituencyPartyVotes(
        election,
        constituency.id.toString()
      )
      partyVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'constituency'

    } else if (province) {
      const res = await ProvinceService.getProvincePartyVotes(election, province.id)
      partyVotesRef.value = res
      currentVoteLevelRef.value = 'province'

    } else {
      const res = await ElectionService.getNationalPartyVotes(election)
      partyVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'national'
    }
  } catch (error) {
    console.error('Error fetching party votes:', error)
    partyVotesRef.value = null
    currentVoteLevelRef.value = null
  }
}

// --- Reset functies per filterset ---
function clearProvinceAndBelow1() {
  selectedProvince1.value = null
  constituencies1.value = []
  selectedConstituency1.value = null
  authorities1.value = []
  selectedAuthority1.value = null
  pollingStations1.value = []
  selectedPollingStation1.value = null
}

function clearConstituencyAndBelow1() {
  selectedConstituency1.value = null
  authorities1.value = []
  selectedAuthority1.value = null
  pollingStations1.value = []
  selectedPollingStation1.value = null
}

function clearAuthorityAndBelow1() {
  selectedAuthority1.value = null
  pollingStations1.value = []
  selectedPollingStation1.value = null
}

function clearPollingStation1() {
  selectedPollingStation1.value = null
}

function clearProvinceAndBelow2() {
  selectedProvince2.value = null
  constituencies2.value = []
  selectedConstituency2.value = null
  authorities2.value = []
  selectedAuthority2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearConstituencyAndBelow2() {
  selectedConstituency2.value = null
  authorities2.value = []
  selectedAuthority2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearAuthorityAndBelow2() {
  selectedAuthority2.value = null
  pollingStations2.value = []
  selectedPollingStation2.value = null
}

function clearPollingStation2() {
  selectedPollingStation2.value = null
}

// --- Handlers eerste filter set ---
async function onElectionChange1() {
  clearProvinceAndBelow1()
  partyVotes1.value = null
  currentVoteLevel1.value = null
  await getProvincesByElection(selectedElection1.value, provinces1, clearProvinceAndBelow1)
}

async function onProvinceChange1() {
  clearConstituencyAndBelow1()
  partyVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedProvince1.value) {
    await getConstituenciesByProvinceId(selectedElection1.value, selectedProvince1.value.id.toString(), constituencies1, clearConstituencyAndBelow1)
  }
}

async function onConstituencyChange1() {
  clearAuthorityAndBelow1()
  partyVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedConstituency1.value) {
    await getAuthoritiesByConstituency(selectedElection1.value, selectedConstituency1.value.id.toString(), authorities1, clearAuthorityAndBelow1)
  }
}

async function onAuthorityChange1() {
  clearPollingStation1()
  partyVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedAuthority1.value) {
    await getPollingStationsByAuthorityId(
      selectedElection1.value,
      selectedConstituency1.value?.id.toString(),
      selectedAuthority1.value.id.toString(),
      pollingStations1,
      clearPollingStation1
    )
  }
}

function onPollingStationChange1() {
  partyVotes1.value = null
  currentVoteLevel1.value = null
}

// --- Handlers tweede filter set ---
async function onElectionChange2() {
  clearProvinceAndBelow2()
  partyVotes2.value = null
  currentVoteLevel2.value = null
  await getProvincesByElection(selectedElection2.value, provinces2, clearProvinceAndBelow2)
}

async function onProvinceChange2() {
  clearConstituencyAndBelow2()
  partyVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedProvince2.value) {
    await getConstituenciesByProvinceId(selectedElection2.value, selectedProvince2.value.id.toString(), constituencies2, clearConstituencyAndBelow2)
  }
}

async function onConstituencyChange2() {
  clearAuthorityAndBelow2()
  partyVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedConstituency2.value) {
    await getAuthoritiesByConstituency(selectedElection2.value, selectedConstituency2.value.id.toString(), authorities2, clearAuthorityAndBelow2)
  }
}

async function onAuthorityChange2() {
  clearPollingStation2()
  partyVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedAuthority2.value) {
    await getPollingStationsByAuthorityId(
      selectedElection2.value,
      selectedConstituency2.value?.id.toString(),
      selectedAuthority2.value.id.toString(),
      pollingStations2,
      clearPollingStation2
    )
  }
}

function onPollingStationChange2() {
  partyVotes2.value = null
  currentVoteLevel2.value = null
}

// --- Functie om filter toe te passen (beide sets) ---
async function applyFilter() {
  if (!selectedElection1.value && !selectedElection2.value) {
    alert('Select at least one election year to show results.')
    return
  }

  if (selectedElection1.value) {
    await fetchPartyVotes(
      selectedElection1.value,
      selectedPollingStation1.value,
      selectedAuthority1.value,
      selectedConstituency1.value,
      selectedProvince1.value,
      partyVotes1,
      currentVoteLevel1
    )
  } else {
    partyVotes1.value = null
    currentVoteLevel1.value = null
  }

  if (selectedElection2.value) {
    await fetchPartyVotes(
      selectedElection2.value,
      selectedPollingStation2.value,
      selectedAuthority2.value,
      selectedConstituency2.value,
      selectedProvince2.value,
      partyVotes2,
      currentVoteLevel2
    )
  } else {
    partyVotes2.value = null
    currentVoteLevel2.value = null
  }
}

</script>

<template>
  <div class="compare-view">
    <h2 style="text-align: center; margin-bottom: 1rem;">Compare Election Results</h2>

    <div class="filters-wrapper">
      <!-- Filter Set 1 -->
      <div class="filter-set">
        <h3>Set 1</h3>

        <select v-model="selectedElection1" @change="onElectionChange1">
          <option value=null disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>

        <select v-if="provinces1.length > 0" v-model="selectedProvince1" @change="onProvinceChange1">
          <option value=null disabled>Select a province</option>
          <option v-for="province in provinces1" :key="province.id" :value="province">{{ province.name }}</option>
        </select>

        <select v-if="constituencies1.length > 0" v-model="selectedConstituency1" @change="onConstituencyChange1">
          <option value=null disabled>Select a constituency</option>
          <option v-for="constituency in constituencies1" :key="constituency.id" :value="constituency">{{ constituency.name }}</option>
        </select>

        <select v-if="authorities1.length > 0" v-model="selectedAuthority1" @change="onAuthorityChange1">
          <option value=null disabled>Select a municipality</option>
          <option v-for="authority in authorities1" :key="authority.id" :value="authority">{{ authority.name }}</option>
        </select>

        <select v-if="pollingStations1.length > 0" v-model="selectedPollingStation1" @change="onPollingStationChange1">
          <option value=null disabled>Select a polling station</option>
          <option v-for="ps in pollingStations1" :key="ps.id" :value="ps">{{ ps.name }}</option>
        </select>
      </div>

      <!-- Filter Set 2 -->
      <div class="filter-set">
        <h3>Set 2</h3>

        <select v-model="selectedElection2" @change="onElectionChange2">
          <option value=null disabled>Select election year</option>
          <option value="2021">2021</option>
          <option value="2023">2023</option>
        </select>

        <select v-if="provinces2.length > 0" v-model="selectedProvince2" @change="onProvinceChange2">
          <option value=null disabled>Select a province</option>
          <option v-for="province in provinces2" :key="province.id" :value="province">{{ province.name }}</option>
        </select>

        <select v-if="constituencies2.length > 0" v-model="selectedConstituency2" @change="onConstituencyChange2">
          <option value=null disabled>Select a constituency</option>
          <option v-for="constituency in constituencies2" :key="constituency.id" :value="constituency">{{ constituency.name }}</option>
        </select>

        <select v-if="authorities2.length > 0" v-model="selectedAuthority2" @change="onAuthorityChange2">
          <option value=null disabled>Select a municipality</option>
          <option v-for="authority in authorities2" :key="authority.id" :value="authority">{{ authority.name }}</option>
        </select>

        <select v-if="pollingStations2.length > 0" v-model="selectedPollingStation2" @change="onPollingStationChange2">
          <option value=null disabled>Select a polling station</option>
          <option v-for="ps in pollingStations2" :key="ps.id" :value="ps">{{ ps.name }}</option>
        </select>
      </div>
    </div>

    <button @click="applyFilter" style="display: block; margin: 1rem auto;">Compare</button>

    <div class="results-wrapper">
      <!-- Placeholder texts for results -->
      <div class="result-set">
        <p v-if="partyVotes1">Results for Set 1 (will be replaced by chart)</p>
        <p v-else>No data for Set 1</p>
      </div>
      <div class="result-set">
        <p v-if="partyVotes2">Results for Set 2 (will be replaced by chart)</p>
        <p v-else>No data for Set 2</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.compare-view {
  padding: 1rem;
}
.filters-wrapper {
  display: flex;
  gap: 2rem;
}
.filter-set {
  border: 1px solid #ccc;
  padding: 1rem;
  border-radius: 5px;
  min-width: 300px;
}
.filter-set select {
  display: block;
  width: 100%;
  margin-bottom: 1rem;
}
</style>
