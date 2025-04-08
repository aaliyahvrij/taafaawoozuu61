<script setup lang="ts">
import { type Ref, ref } from "vue";
import {YearService} from "@/services/YearService.ts";
import type {Election} from "@/interface/Election.ts";



const year: Ref<string> = ref('');
const elections = ref<Election | null >(null)

const emit = defineEmits<{
  (event: 'updateElections', data: Election): void;
}>();

async function fetchNationalPartyVotes(electionId: string){
  const data = await YearService.getPartyVotes(electionId)
  if (data){
    elections.value = data
    emit('updateElections', elections.value);
  }
  else{
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
