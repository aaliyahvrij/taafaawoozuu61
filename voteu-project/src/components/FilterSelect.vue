<!-- components/FilterSelect.vue -->
<script setup lang="ts">
defineProps<{
  label: string
  modelValue: any
  options: any[]
  optionLabelKey?: string
  onChange?: Function
  disabledLabel?: string
}>()

const emit = defineEmits(['update:modelValue', 'clear'])
</script>

<template>
  <div class="filter-select">
    <label>{{ label }}</label>
    <select
      v-if="options.length"
      :value="modelValue"
      @change="e => onChange?.(e.target.value); emit('update:modelValue', options.find(opt => opt.id == e.target.value))"
    >
      <option value="" disabled>{{ disabledLabel || `Select ${label.toLowerCase()}` }}</option>
      <option
        v-for="option in options"
        :key="option.id"
        :value="option.id"
      >
        {{ optionLabelKey ? option[optionLabelKey] : option.name }}
      </option>
    </select>

    <div class="tag" v-if="modelValue">
      {{ optionLabelKey ? modelValue[optionLabelKey] : modelValue.name }}
      <svg
        @click="emit('update:modelValue', null); emit('clear')"
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
