<script setup lang="ts">
import { computed, type Ref, ref } from 'vue'
import type { Province } from '@/interface/Province.ts'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { PollingStation } from '@/interface/PollingStation.ts'
import type { Party } from '@/interface/Party.ts'

import { ElectionService } from '@/services/electiondata/memory/ElectionService.ts'
import { ProvinceService } from '@/services/electiondata/memory/ProvinceService.ts'
import { AuthorityService } from '@/services/electiondata/memory/AuthorityService.ts'
import { PollingStationService } from '@/services/electiondata/memory/PollingStationService.ts'
import { ConstituencyServiceService } from '@/services/electiondata/memory/ConstituencyService.ts'

import RegionFilter from '@/components/filters/RegionFilter.vue'
import BarChartCompare from '@/components/Data/charts/BarChartCompare.vue'

const election1 = ref<string | null>(null)
const province1 = ref<Province | null>(null)
const constituency1 = ref<Constituency | null>(null)
const authority1 = ref<Authority | null>(null)
const pollingStation1 = ref<PollingStation | null>(null)

const filterTags1 = computed(() => {
  const tags = []
  if (election1.value) tags.push({ key: 'election1', label: election1.value })
  if (province1.value) tags.push({ key: 'province1', label: province1.value.name })
  if (constituency1.value) tags.push({ key: 'constituency1', label: constituency1.value.name })
  if (authority1.value) tags.push({ key: 'authority1', label: authority1.value.name })
  if (pollingStation1.value) tags.push({ key: 'pollingStation1', label: pollingStation1.value.name })
  return tags
})

function removeFilter(key: string) {
  switch (key) {
    case 'election1': election1.value = null; break
    case 'province1': province1.value = null; break
    case 'constituency1': constituency1.value = null; break
    case 'authority1': authority1.value = null; break
    case 'pollingStation1': pollingStation1.value = null; break
  }
}


const election2 = ref<string | null>(null)
const province2 = ref<Province | null>(null)
const constituency2 = ref<Constituency | null>(null)
const authority2 = ref<Authority | null>(null)
const pollingStation2 = ref<PollingStation | null>(null)

const filterTags2 = computed(() => {
  const tags = []
  if (election2.value) tags.push({ key: 'election2', label: election2.value })
  if (province2.value) tags.push({ key: 'province2', label: province2.value.name })
  if (constituency2.value) tags.push({ key: 'constituency2', label: constituency2.value.name })
  if (authority2.value) tags.push({ key: 'authority2', label: authority2.value.name })
  if (pollingStation2.value) tags.push({ key: 'pollingStation2', label: pollingStation2.value.name })
  return tags
})

function removeFilter2(key: string) {
  switch (key) {
    case 'election2': election2.value = null; break
    case 'province2': province2.value = null; break
    case 'constituency2': constituency2.value = null; break
    case 'authority2': authority2.value = null; break
    case 'pollingStation2': pollingStation2.value = null; break
  }
}

const partyVotes1 = ref<Party[] | null>(null)
const partyVotes2 = ref<Party[] | null>(null)


async function fetchVotes(election: string | null, ps: PollingStation | null, auth: Authority | null, con: Constituency | null, prov: Province | null, votesRef: Ref<Party[] | null>,): Promise<void> {
  if (!election) return

  try {
    let data: Record<number, Party> | Party[] | null

    if (ps && auth && con) {
      data = await PollingStationService.getPollingStationVotesByAuthorityId(election,con.id.toString(), auth.id, ps.id)
    } else if (auth && con) {
      data = await AuthorityService.getAuthorityVotesByConstituencyId(election, con.id.toString(), auth.id)
    } else if (con) {
      data = await ConstituencyServiceService.getConstituencyPartyVotes(election, con.id.toString())
    } else if (prov) {
      data = await ProvinceService.getProvincePartyVotes(election, prov.id)
    } else {
      data = await ElectionService.getNationalPartyVotes(election)
    }

    if (!data) {
      votesRef.value = null
      return
    }

    if (Array.isArray(data)) {
      votesRef.value = data
    } else {
      votesRef.value = Object.values(data)
    }
  } catch (err) {
    console.error('Error fetching votes:', err)
    votesRef.value = null
  }
}

function applyCompare() {
  fetchVotes(election1.value, pollingStation1.value, authority1.value, constituency1.value, province1.value, partyVotes1)
  fetchVotes(election2.value, pollingStation2.value, authority2.value, constituency2.value, province2.value, partyVotes2)

}
</script>

<template>
  <div class="compare-view">
    <h2>Compare Election Results</h2>

    <div class="filters-wrapper">
      <div class="filter-set">
        <h3>Filter Set 1</h3>
        <RegionFilter
          :election="election1"
          :province="province1"
          :constituency="constituency1"
          :authority="authority1"
          :pollingStation="pollingStation1"
          @update:election="election1 = $event"
          @update:province="province1 = $event"
          @update:constituency="constituency1 = $event"
          @update:authority="authority1 = $event"
          @update:pollingStation="pollingStation1 = $event"
        />
        <div class="filter-tags">
          <span v-for="tag in filterTags1" :key="tag.key" class="tag">
            {{ tag.label }}
            <button @click="removeFilter(tag.key)">x</button>
          </span>
        </div>
      </div>

      <div class="filter-set">
        <h3>Filter Set 2</h3>
        <RegionFilter
          :election="election2"
          :province="province2"
          :constituency="constituency2"
          :authority="authority2"
          :pollingStation="pollingStation2"
          @update:election="election2 = $event"
          @update:province="province2 = $event"
          @update:constituency="constituency2 = $event"
          @update:authority="authority2 = $event"
          @update:pollingStation="pollingStation2 = $event"
        />
        <div class="filter-tags">
          <span v-for="tag in filterTags2" :key="tag.key" class="tag">
            {{ tag.label }}
            <button @click="removeFilter2(tag.key)">x</button>
          </span>
        </div>
      </div>
    </div>

    <button class="compare-button" @click="applyCompare">Compare</button>

    <BarChartCompare :partyVotes1="partyVotes1" :partyVotes2="partyVotes2" />
  </div>
</template>

<style scoped>
.compare-view {
  padding: 1rem;
  background-color: white;
  color: #002970;
}

h2 {
  color: #880D1E;
  margin-bottom: 1rem;
}

.filters-wrapper {
  display: flex;
  flex-direction: row;
  gap: 2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.filter-set {
  flex: 1;
  min-width: 300px;
  border: 2px solid #002970;
  border-radius: 8px;
  padding: 1rem;
}

.filter-set h3 {
  color: #002970;
  margin-bottom: 0.5rem;
}

.filter-tags {
  margin-top: 1rem;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  background-color: #002970;
  color: white;
  border-radius: 20px;
  padding: 0.4rem 0.8rem;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
}

.tag button {
  background: none;
  border: none;
  font-weight: bold;
  margin-left: 0.5rem;
  cursor: pointer;
  font-size: 1rem;
}

.compare-button {
  border: none;
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
}


.chart-container {
  margin-top: 2rem;
}
</style>
