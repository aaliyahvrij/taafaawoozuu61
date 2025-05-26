<script setup lang="ts">
import { ref } from 'vue'
import { ConstiService } from '@/services/ConstiService.ts'
import { AuthorityService } from '@/services/AuthorityService.ts'
import { ElectionService } from '@/services/ElectionService.ts'
import { ProvinceService } from '@/services/ProvinceService.ts'
import { RepUnitService } from '@/services/RepUnitService.ts'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'
import type { Province } from '@/interface/Province.ts'
import type { RepUnit } from '@/interface/RepUnit.ts'

// the first filter set
const selectedElection1 = ref<'2021' | '2023' | null>(null)
const selectedProvince1 = ref<Province | null>(null)
const selectedConsti1 = ref<Constituency | null>(null)
const selectedAuthority1 = ref<Authority | null>(null)
const selectedRepUnit1 = ref<RepUnit | null>(null)
const affiVotes1 = ref<Affiliation[] | null>(null)
const currentVoteLevel1 = ref<
  'national' | 'province' | 'constituency' | 'authority' | 'repUnit' | null
>(null)
const provinces1 = ref<Province[]>([])
const constituencies1 = ref<Constituency[]>([])
const authorities1 = ref<Authority[]>([])
const repUnits1 = ref<RepUnit[]>([])

// the second filter set
const selectedElection2 = ref<'2021' | '2023' | null>(null)
const selectedProvince2 = ref<Province | null>(null)
const selectedConsti2 = ref<Constituency | null>(null)
const selectedAuthority2 = ref<Authority | null>(null)
const selectedRepUnit2 = ref<RepUnit | null>(null)
const affiVotes2 = ref<Affiliation[] | null>(null)
const currentVoteLevel2 = ref<
  'national' | 'province' | 'constituency' | 'authority' | 'repUnit' | null
>(null)

const provinces2 = ref<Province[]>([])
const constituencies2 = ref<Constituency[]>([])
const authorities2 = ref<Authority[]>([])
const repUnits2 = ref<RepUnit[]>([])

// --- Helper functies voor ophalen filters (herbruikbaar) ---
async function getProvincesByElection(
  election: string | null,
  provincesRef: typeof provinces1,
  clearSelectedProvince: () => void,
) {
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

async function getConstituenciesByProvinceId(
  election: string | null,
  provinceId: string | undefined,
  constituenciesRef: typeof constituencies1,
  clearSelectedConstituency: () => void,
) {
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

async function getAuthoritiesByConstituency(
  election: string | null,
  constId: string | undefined,
  authoritiesRef: typeof authorities1,
  clearSelectedAuthority: () => void,
) {
  if (!election || !constId) {
    authoritiesRef.value = []
    clearSelectedAuthority()
    return
  }
  try {
    const response = await AuthorityService.getAuthoritiesByConstId(election, constId)
    authoritiesRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching authorities:', error)
  }
}

async function getRepUnitsByAuthorityId(
  election: string | null,
  constId: string | undefined,
  authorityId: string | undefined,
  repUnitsRef: typeof repUnits1,
  clearSelectedRepUnit: () => void,
) {
  if (!election || !constId || !authorityId) {
    repUnitsRef.value = []
    clearSelectedRepUnit()
    return
  }
  try {
    const response = await RepUnitService.getRepUnitsByAuthorityId(election, constId, authorityId)
    repUnitsRef.value = Array.isArray(response) ? response : Object.values(response || {})
  } catch (error) {
    console.error('Error fetching rep units:', error)
  }
}

// --- Affiliation votes ophalen (herbruikbaar) ---
async function fetchPartyVotes(
  election: string | null,
  repUnit: RepUnit | null,
  authority: Authority | null,
  constituency: Constituency | null,
  province: Province | null,
  affiVotesRef: typeof affiVotes1,
  currentVoteLevelRef: typeof currentVoteLevel1,
) {
  if (!election) {
    affiVotesRef.value = null
    currentVoteLevelRef.value = null
    return
  }
  try {
    if (repUnit && authority && constituency) {
      const res = await RepUnitService.getRepUnitVotesByAuthorityId(
        election,
        constituency.id.toString(),
        authority.id.toString(),
        repUnit.id.toString(),
      )
      affiVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'repUnit'
    } else if (authority && constituency) {
      const res = await AuthorityService.getAuthorityVotesByConstId(
        election,
        constituency.id.toString(),
        authority.id.toString(),
      )
      affiVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'authority'
    } else if (constituency) {
      const res = await ConstiService.getConstiAffiVotes(election, constituency.id.toString())
      affiVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'constituency'
    } else if (province) {
      const res = await ProvinceService.getProvinceAffiVotes(election, province.id)
      affiVotesRef.value = res
      currentVoteLevelRef.value = 'province'
    } else {
      const res = await ElectionService.getNationalLevelAffiVotes(election)
      affiVotesRef.value = Array.isArray(res) ? res : Object.values(res || {})
      currentVoteLevelRef.value = 'national'
    }
  } catch (error) {
    console.error('Error fetching affiliation votes:', error)
    affiVotesRef.value = null
    currentVoteLevelRef.value = null
  }
}

// --- Reset functies per filterset ---
function clearProvinceAndBelow1() {
  selectedProvince1.value = null
  constituencies1.value = []
  selectedConsti1.value = null
  authorities1.value = []
  selectedAuthority1.value = null
  repUnits1.value = []
  selectedRepUnit1.value = null
}

function clearConstituencyAndBelow1() {
  selectedConsti1.value = null
  authorities1.value = []
  selectedAuthority1.value = null
  repUnits1.value = []
  selectedRepUnit1.value = null
}

function clearAuthorityAndBelow1() {
  selectedAuthority1.value = null
  repUnits1.value = []
  selectedRepUnit1.value = null
}

function clearRepUnit1() {
  selectedRepUnit1.value = null
}

function clearProvinceAndBelow2() {
  selectedProvince2.value = null
  constituencies2.value = []
  selectedConsti2.value = null
  authorities2.value = []
  selectedAuthority2.value = null
  repUnits2.value = []
  selectedRepUnit2.value = null
}

function clearConstituencyAndBelow2() {
  selectedConsti2.value = null
  authorities2.value = []
  selectedAuthority2.value = null
  repUnits2.value = []
  selectedRepUnit2.value = null
}

function clearAuthorityAndBelow2() {
  selectedAuthority2.value = null
  repUnits2.value = []
  selectedRepUnit2.value = null
}

function clearRepUnit2() {
  selectedRepUnit2.value = null
}

// --- Handlers eerste filter set ---
async function onElectionChange1() {
  clearProvinceAndBelow1()
  affiVotes1.value = null
  currentVoteLevel1.value = null
  await getProvincesByElection(selectedElection1.value, provinces1, clearProvinceAndBelow1)
}

async function onProvinceChange1() {
  clearConstituencyAndBelow1()
  affiVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedProvince1.value) {
    await getConstituenciesByProvinceId(
      selectedElection1.value,
      selectedProvince1.value.id.toString(),
      constituencies1,
      clearConstituencyAndBelow1,
    )
  }
}

async function onConstituencyChange1() {
  clearAuthorityAndBelow1()
  affiVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedConsti1.value) {
    await getAuthoritiesByConstituency(
      selectedElection1.value,
      selectedConsti1.value.id.toString(),
      authorities1,
      clearAuthorityAndBelow1,
    )
  }
}

async function onAuthorityChange1() {
  clearRepUnit1()
  affiVotes1.value = null
  currentVoteLevel1.value = null
  if (selectedAuthority1.value) {
    await getRepUnitsByAuthorityId(
      selectedElection1.value,
      selectedConsti1.value?.id.toString(),
      selectedAuthority1.value.id.toString(),
      repUnits1,
      clearRepUnit1,
    )
  }
}

function onRepUnitChange1() {
  affiVotes1.value = null
  currentVoteLevel1.value = null
}

// --- Handlers tweede filter set ---
async function onElectionChange2() {
  clearProvinceAndBelow2()
  affiVotes2.value = null
  currentVoteLevel2.value = null
  await getProvincesByElection(selectedElection2.value, provinces2, clearProvinceAndBelow2)
}

async function onProvinceChange2() {
  clearConstituencyAndBelow2()
  affiVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedProvince2.value) {
    await getConstituenciesByProvinceId(
      selectedElection2.value,
      selectedProvince2.value.id.toString(),
      constituencies2,
      clearConstituencyAndBelow2,
    )
  }
}

async function onConstiChange2() {
  clearAuthorityAndBelow2()
  affiVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedConsti2.value) {
    await getAuthoritiesByConstituency(
      selectedElection2.value,
      selectedConsti2.value.id.toString(),
      authorities2,
      clearAuthorityAndBelow2,
    )
  }
}

async function onAuthorityChange2() {
  clearRepUnit2()
  affiVotes2.value = null
  currentVoteLevel2.value = null
  if (selectedAuthority2.value) {
    await getRepUnitsByAuthorityId(
      selectedElection2.value,
      selectedConsti2.value?.id.toString(),
      selectedAuthority2.value.id.toString(),
      repUnits2,
      clearRepUnit2,
    )
  }
}

function onRepUnitChange2() {
  affiVotes2.value = currentVoteLevel2.value = null
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
      selectedRepUnit1.value,
      selectedAuthority1.value,
      selectedConsti1.value,
      selectedProvince1.value,
      affiVotes1,
      currentVoteLevel1,
    )
  } else {
    affiVotes1.value = currentVoteLevel1.value = null
  }
  if (selectedElection2.value) {
    await fetchPartyVotes(
      selectedElection2.value,
      selectedRepUnit2.value,
      selectedAuthority2.value,
      selectedConsti2.value,
      selectedProvince2.value,
      affiVotes2,
      currentVoteLevel2,
    )
  } else {
    affiVotes2.value = null
    currentVoteLevel2.value = null
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
        <select
          v-if="provinces1.length > 0"
          v-model="selectedProvince1"
          @change="onProvinceChange1"
        >
          <option value="null" disabled>Select a province</option>
          <option v-for="province in provinces1" :key="province.id" :value="province">
            {{ province.name }}
          </option>
        </select>
        <select
          v-if="constituencies1.length > 0"
          v-model="selectedConsti1"
          @change="onConstituencyChange1"
        >
          <option value="null" disabled>Select a constituency</option>
          <option
            v-for="constituency in constituencies1"
            :key="constituency.id"
            :value="constituency"
          >
            {{ constituency.name }}
          </option>
        </select>
        <select
          v-if="authorities1.length > 0"
          v-model="selectedAuthority1"
          @change="onAuthorityChange1"
        >
          <option value="null" disabled>Select a municipality</option>
          <option v-for="authority in authorities1" :key="authority.id" :value="authority">
            {{ authority.name }}
          </option>
        </select>
        <select v-if="repUnits1.length > 0" v-model="selectedRepUnit1" @change="onRepUnitChange1">
          <option value="null" disabled>Select a polling station</option>
          <option v-for="ps in repUnits1" :key="ps.id" :value="ps">{{ ps.name }}</option>
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
        <select
          v-if="provinces2.length > 0"
          v-model="selectedProvince2"
          @change="onProvinceChange2"
        >
          <option value="null" disabled>Select a province</option>
          <option v-for="province in provinces2" :key="province.id" :value="province">
            {{ province.name }}
          </option>
        </select>
        <select
          v-if="constituencies2.length > 0"
          v-model="selectedConsti2"
          @change="onConstiChange2"
        >
          <option value="null" disabled>Select a constituency</option>
          <option
            v-for="constituency in constituencies2"
            :key="constituency.id"
            :value="constituency"
          >
            {{ constituency.name }}
          </option>
        </select>
        <select
          v-if="authorities2.length > 0"
          v-model="selectedAuthority2"
          @change="onAuthorityChange2"
        >
          <option value="null" disabled>Select a municipality</option>
          <option v-for="authority in authorities2" :key="authority.id" :value="authority">
            {{ authority.name }}
          </option>
        </select>
        <select v-if="repUnits2.length > 0" v-model="selectedRepUnit2" @change="onRepUnitChange2">
          <option value="null" disabled>Select a polling station</option>
          <option v-for="ps in repUnits2" :key="ps.id" :value="ps">{{ ps.name }}</option>
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
