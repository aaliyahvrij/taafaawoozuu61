<script setup lang="ts">
import { ref, watch } from 'vue'
import { ProvinceService } from '@/services/electiondata/memory/ProvinceService.ts'
import { PollingStationService } from "@/services/electiondata/memory/PollingStationService.ts"
import { AuthorityService } from "@/services/electiondata/memory/AuthorityService.ts"

import type { Province } from '@/interface/Province.ts'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { PollingStation } from '@/interface/PollingStation.ts'

const props = defineProps<{
  election: string | null
  province: Province | null
  constituency: Constituency | null
  authority: Authority | null
  pollingStation: PollingStation | null
}>()

const emit = defineEmits([
  'update:election',
  'update:province',
  'update:constituency',
  'update:authority',
  'update:pollingStation'
])

const provinces = ref<Province[]>([])
const constituencies = ref<Constituency[]>([])
const authorities = ref<Authority[]>([])
const pollingStations = ref<PollingStation[]>([])

watch(() => props.election, async (election) => {
  if (election) {
    const result = await ProvinceService.getProvincesByElection(election)
    provinces.value = result ? Object.values(result) : []
  } else {
    provinces.value = []
  }
})

watch(() => props.province, async (province) => {
  if (props.election && province) {
    const result = await ProvinceService.getConstituenciesByProvinceId(props.election, province.id.toString())
    constituencies.value = result ? Object.values(result) : []
  } else {
    constituencies.value = []
  }
})

watch(() => props.constituency, async (constituency) => {
  if (props.election && constituency) {
    const result = await AuthorityService.getAuthoritiesByConstituencyId(props.election, constituency.id.toString())
    authorities.value = result ? Object.values(result) : []
  } else {
    authorities.value = []
  }
})

watch(() => props.authority, async (authority) => {
  if (props.election && props.constituency && authority) {
    const result = await PollingStationService.getPollingStationsByAuthorityId(
      props.election,
      props.constituency.id.toString(),
      authority.id.toString()
    )
    pollingStations.value = result ? Object.values(result) : []
  } else {
    pollingStations.value = []
  }
})

</script>

<template>
  <div>
    <!-- ELECTION -->
    <select :value="props.election" @change="emit('update:election', ($event.target as HTMLSelectElement).value)">
      <option value="">Select election year</option>
      <option value="2021">2021</option>
      <option value="2023">2023</option>
    </select>

    <!-- PROVINCE -->
    <select v-if="provinces.length" :value="props.province?.id" @change="emit('update:province', provinces.find(p => p.id === Number(($event.target as HTMLSelectElement).value)) || null)">
      <option value="">Select province</option>
      <option v-for="p in provinces" :key="p.id" :value="p.id">{{ p.name }}</option>
    </select>

    <!-- CONSTITUENCY -->
    <select v-if="constituencies.length" :value="props.constituency?.id" @change="emit('update:constituency', constituencies.find(c => c.id === Number(($event.target as HTMLSelectElement).value)) || null)">
      <option value="">Select constituency</option>
      <option v-for="c in constituencies" :key="c.id" :value="c.id">{{ c.name }}</option>
    </select>

    <!-- AUTHORITY -->
    <select v-if="authorities.length" :value="props.authority?.id" @change="emit('update:authority', authorities.find(a => a.id == ($event.target as HTMLSelectElement).value) || null)">
      <option value="">Select municipality</option>
      <option v-for="a in authorities" :key="a.id" :value="a.id">{{ a.name }}</option>
    </select>

    <!-- POLLING STATION -->
    <select v-if="pollingStations.length" :value="props.pollingStation?.id" @change="emit('update:pollingStation', pollingStations.find(p => p.id == ($event.target as HTMLSelectElement).value) || null)">
      <option value="">Select polling station</option>
      <option v-for="p in pollingStations" :key="p.id" :value="p.id">{{ p.name }}</option>
    </select>
  </div>
</template>

<style scoped>
select {
  margin: 0.5rem 0;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border-radius: 8px;
  border: 2px solid #002970;
  background-color: white;
  color: #002970;
  outline: none;
  transition: border-color 0.3s ease;
  width: 100%;
  max-width: 300px;
  display: block;
}

select:focus {
  border-color: #880D1E;
}

option {
  color: #002970;
}
</style>
