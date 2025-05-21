<script setup lang="ts">
import { reactive } from 'vue'

const selectionA = reactive({
year: '',
province: '',
constituency: '',
municipality: '',
})

const selectionB = reactive({
year: '',
province: '',
constituency: '',
municipality: '',
})
</script>

<template>
  <div class="compare-container">
    <!-- Filters -->
    <div class="filters-grid">
      <!-- Selection A -->
      <div class="filter-card border-blue-300">
        <h2 class="filter-title text-blue-700">Selection A</h2>

        <div class="filter-group">
          <label class="filter-label">Election Year</label>
          <select v-model="selectionA.year" class="filter-select">
            <option value="">Select year</option>
            <option value="2021">2021</option>
            <option value="2017">2017</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Province</label>
          <select v-model="selectionA.province" class="filter-select">
            <option value="">Select province</option>
            <option value="Groningen">Groningen</option>
            <option value="Utrecht">Utrecht</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Constituency</label>
          <select v-model="selectionA.constituency" class="filter-select">
            <option value="">Select constituency</option>
            <option value="001">001</option>
            <option value="002">002</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Municipality</label>
          <select v-model="selectionA.municipality" class="filter-select">
            <option value="">Select municipality</option>
            <option value="0100">0100</option>
            <option value="0200">0200</option>
          </select>
        </div>
      </div>

      <!-- Selection B -->
      <div class="filter-card border-red-300">
        <h2 class="filter-title text-red-700">Selection B</h2>

        <div class="filter-group">
          <label class="filter-label">Election Year</label>
          <select v-model="selectionB.year" class="filter-select">
            <option value="">Select year</option>
            <option value="2021">2021</option>
            <option value="2017">2017</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Province</label>
          <select v-model="selectionB.province" class="filter-select">
            <option value="">Select province</option>
            <option value="Groningen">Groningen</option>
            <option value="Utrecht">Utrecht</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Constituency</label>
          <select v-model="selectionB.constituency" class="filter-select">
            <option value="">Select constituency</option>
            <option value="001">001</option>
            <option value="002">002</option>
          </select>
        </div>

        <div class="filter-group">
          <label class="filter-label">Municipality</label>
          <select v-model="selectionB.municipality" class="filter-select">
            <option value="">Select municipality</option>
            <option value="0100">0100</option>
            <option value="0200">0200</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Summary -->
    <div class="summary">
      <p class="summary-label">You are comparing:</p>
      <p class="summary-text">
        <span class="text-blue-700">
          {{ selectionA.year || '...' }} • {{ selectionA.province || '...' }} • {{ selectionA.constituency || '...' }} • {{ selectionA.municipality || '...' }}
        </span>
        <span class="mx-3 text-gray-500">vs</span>
        <span class="text-red-700">
          {{ selectionB.year || '...' }} • {{ selectionB.province || '...' }} • {{ selectionB.constituency || '...' }} • {{ selectionB.municipality || '...' }}
        </span>
      </p>
    </div>

    <!-- Chart placeholder -->
    <div class="chart-placeholder">
      Comparison chart goes here
    </div>
  </div>
</template>

<style scoped>
.compare-container {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
}

.filter-card {
  background-color: white;
  border: 1px solid #e5e7eb;
  border-radius: 1rem;
  padding: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.filter-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.filter-group {
  margin-bottom: 0.75rem;
}

.filter-label {
  display: block;
  font-size: 0.875rem;
  color: #4b5563;
  margin-bottom: 0.25rem;
}

.filter-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
}

.summary {
  text-align: center;
}

.summary-label {
  font-size: 0.875rem;
  color: #6b7280;
}

.summary-text {
  font-size: 1.125rem;
  font-weight: 600;
}

.chart-placeholder {
  height: 16rem;
  background-color: #f3f4f6;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  font-weight: 500;
}
</style>
