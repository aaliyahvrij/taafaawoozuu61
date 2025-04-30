<script setup lang="ts">
import { ref, computed } from 'vue'

// Store the constituencies for each year
const constituenciesByYear= {
  '2021': {
    'Groningen2021': {
      'gemeentes': ["GG1", "GG2"]
    },
    'Lelystad2021': {
      'gemeentes': ["LL1", "LL2"]
    }
  },
  '2023': {
    'Groningen2023': {
      'gemeentes': ["GG1", "GG2", "GG3"]
    },
    'Lelystad2023': {
      'gemeentes': ["LL1", "LL2", "LL3"]
    }
  }
};

const selectedYear = ref<'2021' | '2023' | null>(null);


// This will compute the available constituencies dynamically
const constituencies = computed(function () {
  if (selectedYear.value && constituenciesByYear[selectedYear.value]) {
    return constituenciesByYear[selectedYear.value]
  }
  return []
})

function clearYear(): void {
  selectedYear.value = null
}
</script>

<template>
  <div class="main">
    <label>Select election Year</label>
    <select name="practice" id="practice" v-model="selectedYear">
      <option value="2021">2021</option>
      <option value="2023">2023</option>
    </select>

    <select v-if="constituencies.length > 0">
      <option v-for="(constituency, index) in constituencies" :key="index" :value="constituency">
        {{ constituency }}
      </option>
    </select>

    <div class="tag" v-if="selectedYear !== null">
      {{ selectedYear }}
      <button @click="clearYear()">
        🗙
      </button>
    </div>
  </div>
</template>

<style scoped>
.main {
  color: black;
}
.tag {
  background-color: #002970;
  color: white;
  text-align: center;
  border-radius: 15px;
  width: 100px;
  margin-top: 10px;
}

.tag button {
  background-color: #002970;
  color: white;
  border: none;
}

.tag button:hover {
  cursor: pointer;
}
</style>
