<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  modelValue: '2021' | '2023' | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: '2021' | '2023' | null): void
  (e: 'changed', value: '2021' | '2023' | null): void
  (e: 'cleared'): void
}>()

// Lokale binding
const localValue = ref(props.modelValue)

watch(() => props.modelValue, (newVal) => {
  localValue.value = newVal
})

function onChange() {
  emit('update:modelValue', localValue.value)
  emit('changed', localValue.value)
}

function clear() {
  localValue.value = null
  emit('update:modelValue', null)
  emit('cleared')
}
</script>


<template>
  <div class="election-filter">
    <select class="dropdown" v-model="localValue" @change="onChange">
      <option value="null" disabled>Select an election</option>
      <option value="2021">2021</option>
      <option value="2023">2023</option>
    </select>
    <button class="tag" v-if="localValue">
      {{ localValue }}
      <svg
        @click="clear"
        xmlns="http://www.w3.org/2000/svg"
        height="24px"
        viewBox="0 -960 960 960"
        width="24px"
        fill="#FFFFFF"
      >
        <path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
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

.election-filter {
  min-width: 120px;
  margin-right: 10px;
}

.tag:hover {
  background-color: #00379a;
}
</style>
