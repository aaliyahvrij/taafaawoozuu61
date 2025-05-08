<script setup lang="ts">
import { type Ref, ref } from "vue";
import {YearService} from "@/services/YearService.ts";
import type {Election} from "@/interface/Election.ts";



const year: Ref<string> = ref(''); // jaar begint leeg.
const elections: Ref<Election | null > = ref(null) // variable om gefetchte data op te slaan

const emit = defineEmits(['updateElections']); // definieer emit om op te sturen

async function fetchNationalPartyVotes(electionId: string){ // functie die service aanroept (krijgt id van select)
  const data = await YearService.getNationalPartyVotes(electionId)
  if (data) {
    elections.value = data // variable waarde wordt gezet
    emit('updateElections', elections.value); // emit wordt waarde wordt gezet
  } else {
    elections.value = null;  // If no data, clear elections
  }
}
</script>

<template>
  <div class="main-container">
    <select class="filter-tag" v-model="year" @change="fetchNationalPartyVotes(year)">
      <option value="" disabled selected hidden="">Year</option>
      <option value="2021">2021</option>
      <option value="2023">2023</option>
    </select>
  </div>
</template>

<style scoped>
.filter-tag{
  color: #000000;
  background-color: #efefef;
  border-style: none;
  border-radius: 10px;
  font-family: "Arial";
  min-width: 100px;
  height: 40px;
  font-size:1rem;
  text-align: center;
  margin-right: 10px;
}
</style>
