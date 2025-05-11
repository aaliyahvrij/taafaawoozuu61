<script setup lang="ts">

import { ref } from 'vue'
import { ConstituencyServiceService } from '@/services/ConstituencyService.ts'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import { AuthorityService } from '@/services/AuthorityService.ts'
import { ElectionService } from '@/services/ElectionService.ts'
import type { Party } from '@/interface/Party.ts'
import type { Candidate } from '@/interface/Candidate.ts'


const selectedElection = ref<'2021' | '2023' | null>(null)
const nationalPartyVotes = ref<Party[] | null>(null)
const constituencies = ref<Constituency[]>([])
const selectedConstituency = ref<Constituency| null>(null)
const authorities = ref<Authority[]>([])
const selectedAuthority = ref<Authority| null>(null)
const selectedParty = ref<Party| null>(null)
const selectedCandidate = ref<Candidate| null>(null)

const selectionArray = ref<(string | null)[]>([])
const hasApplied = ref(false)

function handleApply(): void {
  hasApplied.value = true
  selectionArray.value = []

  if (selectedElection.value) selectionArray.value.push(selectedElection.value)
  if (selectedConstituency.value) selectionArray.value.push(selectedConstituency.value.id.toString())
  if (selectedAuthority.value) selectionArray.value.push(selectedAuthority.value.id.toString())

  const length = selectionArray.value.length

  if (length === 1 && selectedElection.value) {
    getNationalPartyVotes(selectedElection.value)
  } else if (length === 2 && selectedElection.value && selectedConstituency.value) {
    //fetchConstituencyPartyVotes(selectedElection.value, selectedConstituency.value.id.toString())
  } else if (length === 3 && selectedElection.value && selectedConstituency.value && selectedAuthority.value) {
   // fetchAuthorityPartyVotes(selectedElection.value,selectedConstituency.value.id.toString(),selectedAuthority.value.id.toString())
  } else {
    console.warn("Invalid selection state.")
  }
}

function clearSelectedElection(): void {
  selectedParty.value = null
  nationalPartyVotes.value = null
  hasApplied.value = false
  selectedElection.value = null
  constituencies.value = []// Reset constituencies when election is cleared
  selectedConstituency.value = null
  authorities.value = []
  selectedAuthority.value = null
}

async function getNationalPartyVotes(electionId: string): Promise<void> {
  if(!hasApplied.value){
    return
  }
  try{
    if(selectedElection.value){
      console.log('Fetching national party votes for election:', electionId)
      const response = await ElectionService.getNationalPartyVotes(electionId)
      console.log(response)
      nationalPartyVotes.value = Array.isArray(response) ? response : Object.values(response || {})
    }
    else{
      console.log("No election selected")
    }

  }
  catch(error){
    console.error("Error fetching national party votes:", error)
  }
}


async function getConstituenciesByElection(election: string | null): Promise<void> {
  try {
    if (election) {
      console.log('Fetching constituencies for election:', election)
      const response = await ConstituencyServiceService.getConstituenciesByElection(election)
      console.log(response)
      constituencies.value = Array.isArray(response) ? response : Object.values(response || {})
    } else {
      console.log("No election selected")
      constituencies.value = []  // Reset constituencies when no election is selected
    }
  } catch (error) {
    console.error("Error fetching constituencies:", error)
  }
}

function clearSelectedConstituency(): void {
  selectedConstituency.value = null
  selectedAuthority.value = null
  authorities.value = []
}


async function getAuthoritiesByConstituency(electionId: string | null, constituencyId: string | undefined): Promise<void> {
  try{
    if (electionId && constituencyId) {
      console.log('Fetching authorities for election:', electionId, 'constituency:', constituencyId)
      const response = await AuthorityService.getAuthoritiesByConstituencyId(electionId, constituencyId)
      console.log(response)
      authorities.value = Array.isArray(response) ? response : Object.values(response || {})
    }
  }
  catch(error){
    console.error("Error fetching authorities:", error)
  }
}

function clearSelectedAuthority(): void {
  selectedAuthority.value = null
}

function handlePartyChange(party: Party): void {
  selectedParty.value = party
  console.log(party)
}

function handleCandidateChange(candidate: Candidate): void {
  console.log(candidate)
  selectedCandidate.value = candidate
}

</script>

<template>
  <div class="filter-bar">
    <!-- Election Filter -->
    <label>Select election: </label>
    <div class="election-filter">
      <select name="election-filter" v-model="selectedElection" @change="getConstituenciesByElection(selectedElection)">
        <option value="" disabled>Select an election</option>
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
      <div class="tag" v-if="selectedElection">
        {{ selectedElection }}
        <svg
          @click="clearSelectedElection()"
          xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
        </svg>
      </div>
    </div>

    <!-- Constituency Filter -->
    <div class="constituency-filter">
      <select v-if="constituencies.length > 0" v-model="selectedConstituency" @change="getAuthoritiesByConstituency(selectedElection, selectedConstituency?.id.toString())">
        <option value="" disabled>Select a constituency</option>
        <option v-for="constituency in constituencies" :key="constituency.id" :value="constituency">
          {{ constituency.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedConstituency">
        {{ selectedConstituency.name }}
        <svg
          @click="clearSelectedConstituency()"
          xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
        </svg>
      </div>
    </div>

    <div class="authority-filter">
      <select v-if="authorities.length > 0" v-model="selectedAuthority">
        <option value="" disabled>Select a municipality</option>
        <option v-for="authority in authorities" :key="authority.id" :value="authority">
          {{ authority.name }}
        </option>
      </select>
      <div class="tag" v-if="selectedAuthority">
        {{ selectedAuthority.name }}
        <svg
          @click="clearSelectedAuthority()"
          xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
        </svg>
      </div>
    </div>

    <div>
      <button @click="handleApply()">
        apply
      </button>
    </div>

  </div>
  <div class="filtered-data">

    <div class="party-list"  v-if="selectedElection && nationalPartyVotes && !selectedParty ">
      <p>National party votes: for Election {{selectedElection}}</p>
      <div class="party-row" v-for="party in nationalPartyVotes" :key="party.id" :value="party"  @click="handlePartyChange(party)">
        {{ party.name }}: <b>{{ party.votes.toLocaleString()}}</b>
      </div>
    </div>

    <div v-if="selectedParty && selectedElection && !selectedCandidate">
      <h1>{{selectedParty.name}}</h1>  <button @click="selectedParty = null"> Back</button>
      <div class="candidate" v-for="candidate in selectedParty.candidates" :key="candidate.id" :value="candidate" @click="handleCandidateChange(candidate)">
        {{candidate.shortCode}} : {{candidate.votes.toLocaleString()}}
      </div>
    </div>

    <div v-if="selectedCandidate && selectedElection">
      <h1>{{selectedCandidate.shortCode}}</h1>
      <h1>votes: {{selectedCandidate.votes.toLocaleString()}}</h1>
      <button @click="selectedCandidate = null"> Back</button>
    </div>

  </div>
</template>
<style scoped>
.candidate {
  border: 1px solid black;
  height: 40px;
}
.candidate:hover {
  background-color: #efefef;
}
.party-list p{
  padding:20px
}

.party-row {
  display: flex;
  align-items: center;
  border: 1px solid black;
  height: 40px;
  padding-left: 10px;
}
.party-row:hover {
  background-color: #efefef;
}

.filtered-data {
  border: 1px solid black;
  height: auto;

}
.filter-bar {
  display: flex;
  flex-direction: row;
}

.election-filter, .constituency-filter, .authority-filter {
  min-width: 120px;
  margin-right: 10px;
}

.election-filter select, .constituency-filter select, .authority-filter select {
  width: 100%;
}

.tag {
  padding: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px; /* optional: space between text and icon */
  background-color: #002970;
  color: white;
  text-align: center;
  border-radius: 15px;
  width: 100%;
  margin-top: 10px;
}

.tag svg {
  cursor: pointer;
}

.tag:hover {
  background-color: #00379a;
}



.tag button:hover {
  cursor: pointer;
}
</style>
