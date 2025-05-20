<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import type { Province } from '@/interface/Province.ts'

const props = defineProps<{
  provinces: Province[]
  selectedProvince: Province | null
}>()

const emit = defineEmits<{
  (e: 'update:selectedProvince', value: Province | null): void
  (e: 'fetchConstituencies', value: Province | null): void
}>()

function handleChange(e: Event) {
  const selectedId = parseInt((e.target as HTMLSelectElement).value)
  const selected = props.provinces.find(p => p.id === selectedId) || null
  emit('update:selectedProvince', selected)
  emit('fetchConstituencies', selected)
}
</script>

<template>
  <div class="province-filter" v-if="provinces.length">
    <select :value="selectedProvince?.id" @change="handleChange">
      <option value="" disabled>Select a province</option>
      <option v-for="province in provinces" :key="province.id" :value="province.id">
        {{ province.name }}
      </option>
    </select>
    <div class="tag" v-if="selectedProvince">
      {{ selectedProvince.name }}
      <svg @click="emit('update:selectedProvince', null)" xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#FFFFFF">
        <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/>
      </svg>
    </div>
  </div>
</template>



<style scoped>
.party-list p {
  padding: 20px;
}
.election-filter select,
.constituency-filter select,
.authority-filter select {
  width: 100%;
}

.tag {
  padding: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px; /* optional: space between text and icon */
  background-color: #002970;
  color: white;
  text-align: center;
  border-radius: 15px;
  width: 100%;
  margin-top: 10px;
}

.tag svg {
  cursor: pointer;
}

.tag:hover {
  background-color: #00379a;
}

.tag button:hover {
  cursor: pointer;
}
</style>
