<script setup lang="ts">

import ConstituencyFilter from "@/components/Data/ConstituencyFilter.vue";
import YearFilter from '@/components/Data/YearFilter.vue'
import {type Ref, ref} from "vue";
import type {Election} from "@/interface/Election.ts";
import type {Party} from "@/interface/Party.ts";
import AuthorityFilter from '@/components/Data/AuthorityFilter.vue'



// Store elections data
const parties :Ref<Party[]> = ref([])
const selectedParty: Ref<Party | null> = ref(null)

// Handle elections data sent from the child
function handleElectionsUpdate(data: Election) {
  parties.value = Object.values(data.nationalParties)
  console.log(parties.value)// Update parent elections data
}

// When clicking a party
function handlePartyClick(party: Party) {
  selectedParty.value = party;
}

function goBack() {
  selectedParty.value = null;
}
</script>

<template>
  <div class="main-template">

    <div class="filters-wrapper">
      <YearFilter  @update-elections="handleElectionsUpdate"/>
      <ConstituencyFilter/>
      <AuthorityFilter/>
    </div>

    <div class="data-wrapper">
      <div class="data-content">


        <!-- Show candidate list if a party is selected -->
        <div v-if="selectedParty">
          <div>
            <strong>{{ selectedParty.name }}</strong> Candidates:
            <button @click="goBack"> Back</button>
          </div>
          <div class="party-list-scroll">
            <div class="party" v-for="candidate in selectedParty.candidates" :key="candidate.id">
              {{ candidate.shortCode}} : {{ candidate.votes.toLocaleString() }} votes
            </div>
          </div>
        </div>

        <div v-if="parties.length > 0">
          <div>Party votes:</div>
          <div class="party-list-scroll">
            <div class="party" v-for="party in parties" :key="party.id" @click="handlePartyClick(party)">
              <div class="party-name"> {{ party.name  }}</div>  : {{ party.votes.toLocaleString() }} votes
            </div>
          </div>
        </div>

        <div v-else>
          <p>Choose your filters.</p>
        </div>

      </div>
    </div>

  </div>
</template>

<style scoped>
.main-template{
  display: flex;
  flex-direction: column;
  height: 600px;
  width: 1000px;
}

.data-wrapper{
  border: #880D1E;
  border-style: solid;
  height: 900px;
}

.data-content{
  color: black;
}

.party-list-scroll{
  max-height: 400px; /* adjust as needed */
  overflow-y: auto;
}

.party{
  display: flex;
  flex-direction: row;
  height: 50px;
  padding: 5px;
}

.party:hover{
  background-color: #002970;
  color: white;
}
.party-name{
  font-weight: bold;
}


.filters-wrapper {
  padding: 2rem;
  margin-left: 0;
  margin-top: 2rem;
  display: flex;
  color:black;
  border-color: #ffffff;
  border-style: solid;
  flex-direction: row;
  background-color: #ffffff;
  width: 1000px;
  height: 150px;


}
</style>
