<script setup lang="ts">
type OptionType = {
  id: string | number
  name?: string
  [key: string]: any
}

const props = defineProps<{
  label: string
  modelValue: OptionType | null
  options: OptionType[]
  optionLabelKey?: string
  disabledLabel?: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: OptionType | null): void
  (e: 'clear'): void
}>()

const handleChange = (event: Event) => {
  const selectedId = (event.target as HTMLSelectElement).value
  const selected = props.options.find(opt => String(opt.id) === selectedId) || null
  emit('update:modelValue', selected)
}
</script>

<template>
  <div class="filter-select">
    <label>{{ label }}</label>

    <select :value="modelValue?.id ?? ''" @change="handleChange">
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
