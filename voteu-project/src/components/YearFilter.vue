<script setup lang="ts">
import {type Ref, ref} from "vue";

const year: Ref<string, string> = ref('');
</script>

<template>
  <div class="main-container">
    <div class="filter-tag">
      select year
      <select v-model="year">
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
    </div>

    <div class="data-container">
      Showing parties of : {{year}}
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
  height: 500px;
  border:1px solid black;
}

</style>
