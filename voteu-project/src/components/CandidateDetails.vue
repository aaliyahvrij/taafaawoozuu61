<script setup lang="ts">
import type { Candidate } from '@/interface/Candidate.ts'

defineProps<{
  selectedCandidate: Candidate
}>()

defineEmits<{
  (e: 'deselect-candidate'): void
}>()
</script>

<template>
  <div class="candidate-details-card">
    <h2 class="candidate-title" v-if="selectedCandidate.shortCode">
      {{ selectedCandidate.shortCode }}
    </h2>
    <h3 class="candidate-name" v-if="selectedCandidate.firstName && selectedCandidate.lastName">
      {{ selectedCandidate.firstName }} {{ selectedCandidate.lastName }}
    </h3>
    <p class="candidate-info">
      <strong>Gender:</strong> {{ selectedCandidate.gender }}
      <svg
        v-if="selectedCandidate.gender === 'male'"
        xmlns="http://www.w3.org/2000/svg"
        height="24px"
        viewBox="0 -960 960 960"
        width="24px"
        fill="#2854C5"
      >
        <path
          d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"
        />
      </svg>
      <svg
        v-if="selectedCandidate.gender === 'female'"
        xmlns="http://www.w3.org/2000/svg"
        height="24px"
        viewBox="0 -960 960 960"
        width="24px"
        fill="#B87E9F"
      >
        <path
          d="M800-800v240h-80v-103L561-505q19 28 29 59.5t10 65.5q0 92-64 156t-156 64q-92 0-156-64t-64-156q0-92 64-156t156-64q33 0 65 9.5t59 29.5l159-159H560v-80h240ZM380-520q-58 0-99 41t-41 99q0 58 41 99t99 41q58 0 99-41t41-99q0-58-41-99t-99-41Z"
        />
      </svg>
      <br />
      <strong>Locality:</strong> {{ selectedCandidate.localityName }}
    </p>
    <p class="candidate-votes">
      Votes: <strong>{{ selectedCandidate.votes.toLocaleString() }}</strong>
    </p>
    <button class="back-button" @click="$emit('deselect-candidate')">Back</button>
  </div>
</template>

<style scoped>
.candidate-details-card {
  background: #f9fafb;

  border-radius: 12px;
  padding: 24px 32px;
  margin: 24px auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.candidate-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
}

.candidate-name {
  font-size: 1.75rem;
  color: #34495e;
  margin-bottom: 16px;
}

.candidate-info {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 16px;
  line-height: 1.5;
}

.candidate-votes {
  font-size: 1.25rem;
  color: #000000;
  margin-bottom: 24px;
}

.back-button {
  background-color: #002970;
  border: none;
  color: white;
  padding: 10px 24px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #0053ba;
  color: white;
}
</style>
