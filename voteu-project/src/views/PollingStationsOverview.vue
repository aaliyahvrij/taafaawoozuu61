<script setup lang="ts">
import { PollingStationService } from '@/services/databaseVotes/PollingStationService.ts'
import { onMounted, ref, watch } from 'vue'
import type { PollingStation } from '@/interface/PollingStation.ts'

const pollingStations = ref<PollingStation[]>([])
const pageNumber = ref(0)
const pageSize = 17
const totalPages = ref()
const zipcode = ref()
const errorMessage = ref('')
const electionId = ref(null)


async function getPollingStations(pageNumber: number): Promise<void> {
  try {
    const response = await PollingStationService.getPollingStations(pageNumber, pageSize,electionId.value)
    pollingStations.value = response.content
    totalPages.value = response.totalPages
  } catch (error) {
    console.error(error)
  }
}

async function searchByZipcode(zipcode:string) : Promise<void>{
  try {
    errorMessage.value = ''
    const response = await PollingStationService.searchPollingStationByZipcode(zipcode,electionId.value)
    pollingStations.value = response
    console.log(response)
  } catch (error : any) {
    errorMessage.value = error.message
  }
}

function nextPage(){
  if(pageNumber.value <921){
    pageNumber.value++
    getPollingStations(pageNumber.value)
  }
}

function previousPage(){
  if(pageNumber.value > 0)
  pageNumber.value--
  getPollingStations(pageNumber.value)
}

// Call on mount
onMounted(() => {
  getPollingStations(pageNumber.value)
})

watch(electionId, () => {
  pageNumber.value = 0
  getPollingStations(pageNumber.value)
})

</script>

<template>
  <b><p v-if="errorMessage" style="color:red;">{{ errorMessage }}</p></b>
  <div class="pollingstations-header">
    <select class="election-select"  v-model="electionId">
      <option value="null" disabled>Select Election</option>
      <option value="TK2021">2021</option>
      <option value="TK2023">2023</option>
    </select>
    <div class="pollingstations-title">
      <p v-if="electionId === null">Pollingstations of 2021</p>
      <p v-else-if="electionId === 'TK2021'">Pollingstations of 2021</p>
      <p v-else-if="electionId === 'TK2023'">Pollingstations of 2023</p>
    </div>

    <div v-if="pollingStations.length > 0 && !zipcode">
      <div class="arrow-container">
        <button class="pagenation" :disabled="pageNumber === 0" @click="previousPage()">previous</button>
        <button class="pagenation" @click="nextPage()">next</button>
      </div>
      <div class="page-count">
        {{pageNumber + 1}} / {{totalPages}}
      </div>
    </div>
  </div>
  <table>
    <thead>
    <tr>
      <th class="name-column">Polling Station name</th>
      <th class="zipcode-column">Zipcode
        <input placeholder="Search zipcode"  class="search-zipcode" v-model="zipcode" @input="errorMessage= '' ">
        <button class="search-button" @click="searchByZipcode(zipcode)">
          Search
        </button>
        <button class="clear-button" v-if="zipcode" @click="getPollingStations(pageNumber); zipcode='' ">
          Clear
        </button>
      </th>
      <th class="municipality-column">Municipality</th>
    </tr>
    </thead>
    <tbody>
    <tr class="pollingstation-row" v-for="pollingstation in pollingStations" :key="pollingstation.id">
      <td>{{ pollingstation.name }}</td>
      <td>{{ pollingstation.zipcode }}</td>
      <td>{{ pollingstation.authorityName }}</td>
    </tr>
    </tbody>
  </table>
  <b><p v-if="pollingStations.length < 1">No polling stations found for this location.</p></b>

</template>
<style scoped>

.election-select{
  height: 40px;
  width: 200px;
  margin-bottom: 15px;
}

.pollingstations-header{
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  margin:20px
}

.pollingstations-title{
  margin: 0px 25px 20px 25px;
}

table {
  width: 100%;
  border-collapse: separate;
  margin-top: 1rem;
  border-radius: 12px;
}

th,
td {
  border: 1px solid #ccc;
  padding: 8px 12px;
  text-align: left;
}

th{
  height: 50px;
}

.zipcode-column{
  display: flex;
  align-items: center;
}
.name-column{
  width: 40%;
}
.municipality-column{
  width:40%
}



thead {
  background-color: #f0f0f0;
}

tbody tr:nth-child(even) {
  background-color: #fafafa;
}

.arrow-container{
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagenation{
  height: 40px;
  width: 80px;
  margin: 10px;
}
.page-count{
  display: flex;
  justify-content: center;
}

.search-zipcode{
padding: 7px ;
  margin-left: 3px;
}

.pollingstation-row:hover{
  background-color: #002b80;
  color:white;
}

.search-button:hover , .clear-button:hover{
  cursor: pointer;
  transform: scale(1.2);
}

.search-button, .clear-button{
  margin-right: 5px;
  margin-left: 5px;
  padding-left:10px;
  padding-right:10px
}


</style>
