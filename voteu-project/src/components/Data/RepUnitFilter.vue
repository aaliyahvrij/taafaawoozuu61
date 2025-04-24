<script setup lang="ts">
import { type Ref, ref } from 'vue'
import type { ReportingUnit } from '@/interface/ReportingUnit.ts'
import { RepUnitService } from '@/services/RepUnitService.ts'

//:Ref<TYPE> = ref(value)
const year: Ref<string> = ref('') // waarde wordt veranderd met select
const repUnit: Ref<ReportingUnit | null> = ref(null)

const emit = defineEmits<{
  (event: 'updateRepUnits', data: ReportingUnit): void
}>()

async function fetchRepUnits() {
  const data = await RepUnitService.getReportingUnits()
  if (data) {
    repUnit.value = data
    emit('updateRepUnits', repUnit.value)
  } else {
    repUnit.value = null // If no data, clear reporting units
  }
}
</script>

<template>
  <div class="main-container">
    <select class="filter-tag" v-model="year" @change="fetchRepUnits">
      <option value="" disabled selected hidden="">ReportingUnit</option>
      <option value="2023">Stembureaus</option>
    </select>
  </div>
</template>

<style scoped>
.filter-tag {
  color: #000000;
  background-color: #efefef;
  border-style: none;
  border-radius: 10px;
  font-family: 'Arial';
  min-width: 100px;
  height: 40px;
  font-size: 1rem;
  text-align: center;
  margin-right: 10px;
}
</style>
