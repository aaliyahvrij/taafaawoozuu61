<script setup lang="ts">
import FilterSelect from '@/components/FilterSelect.vue'
import { ConstituencyService } from '@/services/ConstituencyService.ts'
import type { DropdownOption } from '@/interface/DropdownOption'
import { ref, watch, onMounted } from 'vue'
import VoteListOverview from '@/components/filters/VoteListOverview.vue'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'
import type { Candidate } from '@/interface/Candidate.ts'
import { ElectionsService } from '@/services/databaseVotes/ElectionsService.ts'
import { ProvincesService } from '@/services/databaseVotes/ProvincesService.ts'
const elections = ref<DropdownOption[]>([])
const selectedElection = ref<DropdownOption | null>(null)
const provinces = ref<DropdownOption[]>([])
const selectedProvince = ref<DropdownOption | null>(null)
const constituencies = ref<DropdownOption[]>([])
const selectedConstituency = ref<DropdownOption | null>(null)
const nationalVotes = ref<PartyVotesDTO[]>([])
const partyVotes = ref<PartyVotesDTO[]>([]) // placeholder
const selectedParty = ref<string>('') // placeholder
const selectedCandidate = ref<Candidate | null>(null) // placeholder
const currentVoteLevel = ref<string>('') // will hold the string 'national',
const voteLevels = ref<(string | number)[]>([])

onMounted(async () => {
  const data = await ElectionsService.getElectionNames()
  if (data) {
    elections.value = data
    console.log(data)
  }
})

async function fetchProvinceOptions(electionId: string) : Promise<void> {
  try {
    const data = await ProvincesService.getProvinceNames(electionId);
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

async function fetchNationalVotes(electionId: string ): Promise<void> {
  try {
    const data = await ElectionsService.getNationalPartyVotes(electionId)
    nationalVotes.value = data || []
    currentVoteLevel.value = `national`
    console.log(data)

  } catch (e){
    console.error(e)
  }
}

async function fetchProvincePartyVotes(electionId: string , provinceId: number): Promise<void> {
  try {
    const data = await ProvincesService.getProvincePartyVotes(electionId, provinceId)
    nationalVotes.value = data || []
    currentVoteLevel.value = `national`
    console.log(data)

  } catch (e){
    console.error(e)
  }
}


watch(selectedElection, (newElection) => {
  if (newElection) {
    voteLevels.value = [newElection.id]
    fetchProvinceOptions(String(newElection.id))
  } else {
    voteLevels.value = []
    provinces.value = []
    constituencies.value = []
  }
})

watch(selectedProvince, (newProvince) => {
  if (newProvince && selectedElection.value) {
    voteLevels.value = [selectedElection.value.id, newProvince.id]
    fetchConstituencyOptions(String(selectedElection.value.id), Number(newProvince.id))
  } else if (selectedElection.value) {
    voteLevels.value = [selectedElection.value.id]
    constituencies.value = []
  }
})

watch(selectedConstituency, (newConstituency) => {
  if (newConstituency && selectedElection.value && selectedProvince.value) {
    voteLevels.value = [
      selectedElection.value.id,
      selectedProvince.value.id,
      newConstituency.id
    ]
  } else if (selectedElection.value && selectedProvince.value) {
    voteLevels.value = [
      selectedElection.value.id,
      selectedProvince.value.id
    ]
  }
})



const isPartyListVisible = ref(false)

function handleApply() {
  if (!selectedElection.value) {
    alert("Please select an election.");
    return;
  }

  if (selectedProvince.value) {
    // Fetch votes for selected provinces
    fetchProvincePartyVotes(
      String(selectedElection.value.id),
      Number(selectedProvince.value.id)
    );
  } else {
    // Fetch national votes if no province selected
    fetchNationalVotes(String(selectedElection.value.id));
  }

  isPartyListVisible.value = true;
}



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

    <div class="apply-button-wrapper">
      <button class="apply-button" @click="handleApply">Apply</button>
    </div>


  </div>
  <VoteListOverview
    v-if="isPartyListVisible"
                    :selected-election="selectedElection?.id ?? null"
                    :displayed-party-votes="nationalVotes"
                    :party-votes="partyVotes"
                    :selected-party="selectedParty"
                    :selected-candidate="selectedCandidate"
                    :current-vote-level="currentVoteLevel"/>
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
