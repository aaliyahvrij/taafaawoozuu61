<script setup lang="ts">
import type { DropdownOption } from '@/interface/DropdownOption.ts'

// receives from parent
const props = defineProps<{
  modelValue: DropdownOption | null
  options: DropdownOption[]
  disabledLabel?: string
}>()

//gives to parent
const emit = defineEmits<{
  (e: 'update:modelValue', value: DropdownOption | null): void
  (e: 'clear'): void
}>()

// Normal function to handle change event
function handleChange(event: Event) {
  // Tell TypeScript that event.target is an HTMLSelectElement, so we can access `.value`
  const selectElement = event.target as HTMLSelectElement
  const selectedId = selectElement.value
  // Find the selected option from the option list by matching the id as a string
  const selectedOption = props.options.find(function(opt) {
    return String(opt.id) === selectedId
  }) || null

  // Emit the update event to update a v-model
  emit('update:modelValue', selectedOption)
}

// Clear selection when clicking the clear button
function clearSelection() {
  emit('update:modelValue', null)
  emit('clear')
}
</script>

<template>
  <div class="filter-select">
    <select :value="modelValue?.id ?? ''" @change="handleChange">
      <option value="" disabled>{{ disabledLabel ?? 'Select Option' }}</option>
      <option
        v-for="option in options"
        :key="option.id"
        :value="option.id"
      >
        {{ option.name }}
      </option>
    </select>

    <div class="tag" v-if="modelValue">
      {{ modelValue.name }}
      <svg
        @click="clearSelection"
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
    </div>
  </div>
</template>

<style>
select {
  margin: 0.5rem 0;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border-radius: 8px;
  border: 2px solid #002970;
  background-color: white;
  color: #002970;
  outline: none;
  transition: border-color 0.3s ease;
  width: 100%;
  max-width: 300px;
  display: block;
}

.tag {
  background-color: #002970;
  color: white;
  border-radius: 20px;
  padding: 0.4rem 0.8rem;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  cursor: default;
}
.tag svg {
  cursor: pointer;
  margin-left: 0.5rem;
}
</style>
