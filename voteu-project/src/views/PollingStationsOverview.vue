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
      <th>Polling Station name</th>
      <th>Zipcode
        <input class="search-zipcode" v-model="zipcode" @input="errorMessage= '' ">
        <button v-if="zipcode"
                @click="getPollingStations(pageNumber); zipcode='' "
        >clear</button>
        <button class="search-button" @click="searchByZipcode(zipcode)">🔍</button></th>
      <th>Municipality</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="pollingstation in pollingStations" :key="pollingstation.id">
      <td>{{ pollingstation.name }}</td>
      <td>{{ pollingstation.zipcode }}</td>
      <td>{{ pollingstation.authorityName }}</td>
    </tr>
    </tbody>
  </table>

  <div class="arrow-container">
    <button class="pagenation" :disabled="pageNumber === 0" @click="previousPage()">previous</button>
    <button class="pagenation" @click="nextPage()">next</button>
  </div>
  <div class="page-count">
    {{pageNumber + 1}} / {{totalPages}}
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
padding: 10px ;
}


</style>
