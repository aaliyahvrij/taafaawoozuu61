<script setup lang="ts">
import FilterSelect from '@/components/FilterSelect.vue'
import { ElectionService } from '@/services/ElectionService.ts'
import { ProvinceService } from '@/services/ProvinceService.ts'
import { ConstituencyService } from '@/services/ConstituencyService.ts'
import type { DropdownOption } from '@/interface/DropdownOption'
import { ref, watch, onMounted } from 'vue'
import VoteListOverview from '@/components/filters/VoteListOverview.vue'

const elections = ref<DropdownOption[]>([])
const selectedElection = ref<DropdownOption | null>(null)
const provinces = ref<DropdownOption[]>([])
const selectedProvince = ref<DropdownOption | null>(null)
const constituencies = ref<DropdownOption[]>([])
const selectedConstituency = ref<DropdownOption | null>(null)
onMounted(async () => {
  const data = await ElectionService.getElectionNames()
  if (data) {
    elections.value = data
    console.log(data)
  }
})

async function fetchProvinceOptions(electionId: string) : Promise<void> {
  try {
    const data = await ProvinceService.getProvinceNames(electionId);
    provinces.value = data || []
  } catch (e) {
    console.error(e)
  }
}

async function fetchConstituencyOptions(electionId: string, provinceId: number): Promise<void> {
  try {
    const data = await ConstituencyService.getConstituencyNames(electionId, provinceId)
    constituencies.value = data || []
  } catch (e) {
    console.error(e)
  }
}

watch(selectedElection, (newElection) => {
  console.log('selectedElection changed:', newElection);
  if (newElection && newElection.id) {
    fetchProvinceOptions(String(newElection.id))
  } else {
    provinces.value = []
  }
})

  watch([selectedElection, selectedProvince], ([newElection, newProvince]) => {
    if (newElection?.id && newProvince?.id) {
      fetchConstituencyOptions(String(newElection.id), Number(newProvince.id))
    } else {
      constituencies.value = []
      selectedConstituency.value = null
    }
  })
</script>

<template>
  <div class="filter-bar">

    <div class="election-filter">
      <FilterSelect
        v-model="selectedElection"
        :options="elections"
        optionLabelKey="name"
        disabledLabel="Select election"
      />
    </div>

    <div class="province-filter" v-if="provinces.length">
      <FilterSelect
        v-model="selectedProvince"
      :options="provinces"
      disabledLabel="Select province"
      />
    </div>

    <div class="constituency-filter" v-if="constituencies.length">
      <FilterSelect
        v-model="selectedConstituency"
        :options="constituencies"
        disabledLabel="Select constituency"
      />
    </div>


  </div>
  <vote-list-overview selected-election="selectedElection" displayed-party-votes="" party-votes="" selected-party="" selected-candidate="" current-vote-level=""</v>
</template>

<style scoped>
.buttons button {
  margin: 0.5rem;
}

.apply-button {
  background-color: #66817d;
  color: #ffffff;
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  padding: 0.5rem;
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  font-size: 1rem;
}

.apply-button:hover {
  background-color: #0053ba;
  color: white;
}

.party-list p {
  margin: 0;
  font-weight: 500;
  font-size: 1.1rem;
  color: #333;
}

.filter-bar {
  display: flex;
  flex-direction: row;
}

.election-filter select,
.constituency-filter select,
.authority-filter select {
  width: 100%;
}

.filter-bar div {
  margin-right: 10px;
}

.tag svg {
  cursor: pointer;
}

.tag button:hover {
  cursor: pointer;
}
</style>
