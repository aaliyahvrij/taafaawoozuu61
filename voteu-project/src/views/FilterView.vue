<script setup lang="ts">

import ConstituencyFilter from "@/components/Data/ConstituencyFilter.vue";
import YearFilter from '@/components/Data/YearFilter.vue'
import {ref} from "vue";
import type {Election} from "@/interface/Election.ts";
import type {Party} from "@/interface/Party.ts";



// Store elections data
const parties = ref<Party[]>([])

// Handle elections data sent from the child
function handleElectionsUpdate(data: Election) {
  parties.value = data.parties;// Update parent elections data
}
</script>

<template>
  <div class="main-template">

  <div class="filters-wrapper">
    <YearFilter  @update-elections="handleElectionsUpdate"/>
    <ConstituencyFilter/>
  </div>

    <div class="data-wrapper">
      <div class="data-content">

        <div v-if="parties.length > 0">
          <div>Party votes:</div>
          <div class="party-list-scroll">
          <div class="party" v-for="party in parties" :key="party.id">
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
