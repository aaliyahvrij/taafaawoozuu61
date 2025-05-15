<script setup lang="ts">
import ConstituencyFilter from '@/components/Data/ConstituencyFilter.vue'
import YearFilter from '@/components/Data/YearFilter.vue'
import { type Ref, ref } from 'vue'
import type { Election } from '@/interface/Election.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'
import RepUnitFilter from '@/components/Data/RepUnitFilter.vue'

// Store elections data
const affiliations: Ref<Affiliation[]> = ref([])
const selectedAffiliation: Ref<Affiliation | null> = ref(null)

// Handle elections data sent from the child
function handleElectionsUpdate(data: Election) {
  affiliations.value = Object.values(data.nationalParties)
  console.log(affiliations.value) // Update parent elections data
}

// When clicking an affiliation
function handleAffiClick(affiliation: Affiliation) {
  selectedAffiliation.value = affiliation
}

function goBack() {
  selectedAffiliation.value = null
}
</script>

<template>
  <div class="main-template">
    <div class="filters-wrapper">
      <YearFilter @update-elections="handleElectionsUpdate" />
      <ConstituencyFilter />
      <RepUnitFilter />
    </div>

    <div class="data-wrapper">
      <div class="data-content">
        <!-- Show candidate list if an affiliation is selected -->
        <div v-if="selectedAffiliation">
          <div>
            <strong>{{ selectedAffiliation.name }}</strong> Candidates:
            <button @click="goBack">Back</button>
          </div>
          <div class="affi-list-scroll">
            <div class="affiliation" v-for="candidate in selectedAffiliation.candidates" :key="candidate.id">
              {{ candidate.shortCode }} : {{ candidate.votes.toLocaleString() }} votes
            </div>
          </div>
        </div>

        <div v-if="affiliations.length > 0">
          <div>Affiliation votes:</div>
          <div class="affi-list-scroll">
            <div
              class="affiliation"
              v-for="affiliation in affiliations"
              :key="affiliation.id"
              @click="handleAffiClick(affiliation)"
            >
              <div class="affi-name">{{ affiliation.name }}</div>
              : {{ affiliation.votes.toLocaleString() }} votes
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
.main-template {
  display: flex;
  flex-direction: column;
  height: 600px;
  width: 1000px;
}

.data-wrapper {
  border: #880d1e;
  border-style: solid;
  height: 900px;
}

.data-content {
  color: black;
}

.affi-list-scroll {
  max-height: 400px; /* adjust as needed */
  overflow-y: auto;
}

.affiliation {
  display: flex;
  flex-direction: row;
  height: 50px;
  padding: 5px;
}

.affiliation:hover {
  background-color: #002970;
  color: white;
}

.affi-name {
  font-weight: bold;
}

.filters-wrapper {
  padding: 2rem;
  margin-left: 0;
  margin-top: 2rem;
  display: flex;
  color: black;
  border-color: #ffffff;
  border-style: solid;
  flex-direction: row;
  background-color: #ffffff;
  width: 1000px;
  height: 150px;
}
</style>
