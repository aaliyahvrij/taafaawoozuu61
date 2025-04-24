<script setup lang="ts">
import { type Ref, ref } from "vue";
import { AuthorityService } from "@/services/AuthorityService.ts";
import type { Authority } from "@/interface/Authority.ts";

// Refs for storing data
const year: Ref<string> = ref('');
const authorities: Ref<Authority[] | null> = ref(null)

// Fetch authority votes based on electionId
async function fetchAuthorityVotes(electionId: string) {
  const data = await AuthorityService.getAuthorityVotes(electionId);

  if (data) {
   authorities.value = Object.values(data)
    for(let i = 0; i<authorities.value.length; i++) {
      console.log(authorities.value[i].name)
    }
  } else {
    authorities.value = null;
    console.warn(`No authorities data for electionId: ${electionId}`);
  }
}
</script>


<template>
  <div class="main-container">
    <select class="filter-tag" v-model="year" @change="fetchAuthorityVotes(year)">
      <option value="" disabled selected hidden="">Municipality</option>
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
