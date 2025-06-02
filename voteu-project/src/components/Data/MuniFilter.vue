<script setup lang="ts">
import { type Ref, ref } from 'vue'
import { MuniService } from '@/services'
import type { Municipality } from '@/interfaces'

// Refs for storing data
const year: Ref<string> = ref('')
const municipalities: Ref<Municipality[] | null> = ref(null)
const emit = defineEmits(['updateAuthorities'])

// Fetch authority votes based on electionId
async function fetchAuthorityVotes(electionId: string) {
  const data = await MuniService.getAuthorityVotes(electionId)
  if (data) {
    municipalities.value = Object.values(data)
    console.log(municipalities.value)
    emit('updateAuthorities', municipalities.value)
  } else {
    municipalities.value = null
    console.warn(`No authorities data for electionId: ${electionId}`)
  }
}
</script>

<template>
  <div class="main-container">
    <select class="filter-tag" v-model="year" @change="fetchAuthorityVotes(year)">
      <option value="" disabled selected hidden="">MunicipalityYear</option>
      <option value="2021">2021</option>
      <option value="2023">2023</option>
    </select>

    <select class="filter-tag" v-model="year" @change="fetchAuthorityVotes(year)">
      <option value="" disabled selected hidden="">Municipality</option>
      <option v-for="muni in municipalities || []" :key="muni.id" :value="muni.id">
        {{ muni.name }}
      </option>
    </select>
  </div>
</template>

<style scoped>
.filter-tag {
  height: 40px;
  min-width: 100px;
  color: #000000;
  background-color: #efefef;
  border-style: none;
  border-radius: 10px;
  font-family: 'Arial';
  font-size: 1rem;
  text-align: center;
  margin-right: 10px;
}
</style>
