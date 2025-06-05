<script setup lang="ts">
import { ref, watch } from 'vue'
import type { PollingStation } from '@/interface/PollingStation.ts'

const props = defineProps<{
  modelValue: PollingStation | null
  options: PollingStation[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: PollingStation | null): void
  (e: 'cleared'): void
}>()

const localValue = ref<PollingStation | null>(props.modelValue)

watch(() => props.modelValue, (newVal) => {
  localValue.value = newVal
})

function onChange() {
  emit('update:modelValue', localValue.value)
}

function clear() {
  localValue.value = null
  emit('update:modelValue', null)
  emit('cleared')
}
</script>

<template>
  <div class="polling-station-filter">
    <select class="dropdown" v-if="options.length > 0" v-model="localValue" @change="onChange">
      <option value="null" disabled>Select a polling station</option>
      <option
        v-for="pollingStation in options"
        :key="pollingStation.id"
        :value="pollingStation"
      >
        {{ pollingStation.name }}
      </option>
    </select>
    <button class="tag" v-if="localValue">
      {{ localValue.name }}
      <svg
        @click="clear"
        xmlns="http://www.w3.org/2000/svg"
        height="24px"
        viewBox="0 -960 960 960"
        width="24px"
        fill="#FFFFFF"
      >
        <path
          d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
        />
      </svg>
    </button>
  </div>
</template>


<style scoped>
/* gebruik dezelfde CSS zoals je had, of importeer globaal */
.dropdown {
  width: 100%;
  padding: 0.5rem;
  margin: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
}
.tag {
  padding: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background-color: #002970;
  color: white;
  border-radius: 15px;
  width: 100%;
  margin: 0.5rem;
}
.tag svg {
  cursor: pointer;
}
.tag:hover {
  background-color: #00379a;
}
</style>
