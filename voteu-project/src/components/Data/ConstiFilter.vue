<script setup lang="ts">
//import { ref, onMounted } from "vue";
//import ConstituencyService from "@/services/ConstituencyService";
/*import type { ConstituencyFilter } from "@/services/ConstituencyService";  // Gebruik 'type' om de interface te importeren

//const constituencies = ref<ConstiFilter[]>([]);

// Fetch constituencies
const getConstituency = async () => {
  try {
    const response = await ConstituencyService.getConstituency();
    constituencies.value = response.data; // Dit zou nu een array van ConstituencyFilter moeten zijn
  } catch (error) {
    console.error("Error fetching constituencies:", error);
  }
};

onMounted(getConstituency);
*/

</script>

<template>
  <div class="main-container">
      <select class="filter-tag">
        <option value="" disabled selected hidden="">Constituency</option>
        <option value="Amsterdam">Amsterdam</option>
        <option value="Groningen">Groningen</option>
      </select>

  </div>
  <!-- <p v-if="constituencies.length > 0">{{ constituencies[0].name }} </p> -->   <!-- Zorg ervoor dat je toegang hebt tot een specifiek veld zoals name -->
  <!-- <p v-else>Loading...</p> -->
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
}
</style>
