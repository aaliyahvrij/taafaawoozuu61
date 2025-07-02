<script setup lang="ts">
import { PollingStationService } from '@/services/databaseVotes/PollingStationService.ts'
import { onMounted, ref } from 'vue'
import type { PollingStation } from '@/interface/PollingStation.ts'

const pollingStations = ref<PollingStation[]>([])
const pageNumber = ref(0)
const pageSize = 20
const totalPages = ref()
const zipcode = ref()
const errorMessage = ref('')

async function getPollingStations(pageNumber: number): Promise<void> {
  try {
    const response = await PollingStationService.getPollingStations(pageNumber, pageSize)
    pollingStations.value = response.content
    totalPages.value = response.totalPages
  } catch (error) {
    console.error(error)
  }
}

async function searchByZipcode(zipcode:string) : Promise<void>{
  try {
    errorMessage.value = ''
    const response = await PollingStationService.searchPollingStationByZipcode(zipcode)
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

</script>

<template>
  <b><p v-if="errorMessage" style="color:red;">{{ errorMessage }}</p></b>
  <table>
    <thead>
    <tr>
      <th class="name-column">Polling Station name</th>
      <th class="zipcode-column">Zipcode
        <input class="search-zipcode" v-model="zipcode" @input="errorMessage= '' ">
        <button v-if="zipcode" @click="getPollingStations(pageNumber); zipcode='' ">
          <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="m336-280 144-144 144 144 56-56-144-144 144-144-56-56-144 144-144-144-56 56 144 144-144 144 56 56ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/></svg>
        </button>
        <button class="search-button" @click="searchByZipcode(zipcode)">
          <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#000000"><path d="M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"/></svg>
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
  <div v-if="pollingStations.length > 0 && !zipcode">
    <div class="arrow-container">
      <button class="pagenation" :disabled="pageNumber === 0" @click="previousPage()">previous</button>
      <button class="pagenation" @click="nextPage()">next</button>
    </div>
    <div class="page-count">
      {{pageNumber + 1}} / {{totalPages}}
    </div>
  </div>

</template>
<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
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
  height: 60px;
  width: 200px;
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


</style>
