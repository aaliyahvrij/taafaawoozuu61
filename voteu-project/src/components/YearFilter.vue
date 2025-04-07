<script setup lang="ts">
import {type Ref, ref} from "vue";
import {YearService} from "@/services/YearService.ts";

const year: Ref<string, string> = ref('');

async function fetchContest(electionId: string) {
  const data =  await YearService.getContestSummary(electionId)
  contests.value = data;
  console.log(data)
}

const contests = ref()


</script>

<template>
  <div class="main-container">
    <div class="filter-tag">
      select year
      <select v-model="year" @change="fetchContest(year)">
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
    </div>

    <div class="data-container">
      Showing contest of : {{year}}
      <div v-for="contest in contests" :key="contest.contestName"  >
        {{contest.contestName}}
        , {{contest.partyCount}} parties participating
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-container{
  border:1px solid black;
}

.filter-tag{
  border:1px solid black;
  width: 200px;
  height:50px
}

select{
  width: 100%;
}

.data-container{
  height: auto;
  border:1px solid black;
}

</style>
