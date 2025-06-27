<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAllReports, deleteReport } from '@/services/ReportsService.ts'
import type { Report } from '@/interface/Report'
import { blockUser } from '@/services/UserService.ts'

const reports = ref<Report[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)
const isDeleting = ref<number | null>(null)

const fetchReports = async () => {
  try {
    reports.value = await getAllReports()
    error.value = null
  } catch (err) {
    console.error('Failed to fetch reports:', err);
    error.value = 'Failed to fetch reports.'
  } finally {
    isLoading.value = false
  }
}

const handleDelete = async (id: number) => {
  if (!confirm('Are you sure you want to delete this report?')) return
  isDeleting.value = id
  try {
    await deleteReport(id)
    reports.value = reports.value.filter(r => r.id !== id)
    alert('Report deleted.')
  } catch (err) {
    console.error('Failed to fetch deleted reports:', err);
    alert('Failed to delete report.')
  } finally {
    isDeleting.value = null
  }
}

const handleBlock = async (userId: number) => {
  if (!confirm('Are you sure you want to block this user?')) return;
  try {
    await blockUser(userId);
    alert('User has been blocked.');
  } catch (err) {
    console.error('Failed to fetch blocked users:', err);
    alert('Failed to block user.');
  }
};

onMounted(() => fetchReports())
</script>

<template>
  <div class="reports-container">
    <h2>User Reports</h2>

    <div v-if="isLoading">Loading...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <table v-else>
      <thead>
      <tr>
        <th>ID</th>
        <th>Reporter ID</th>
        <th>Reported ID</th>
        <th>Reason</th>
        <th>Created At</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="report in reports" :key="report.id">
        <td>{{ report.id }}</td>
        <td>{{ report.reporter_user_id }}</td>
        <td>{{ report.reported_user_id }}</td>
        <td>{{ report.reason }}</td>
        <td>{{ new Date(report.created_at).toLocaleString() }}</td>
        <td>
          <button
            class="delete-button"
            :disabled="isDeleting === report.id"
            @click="handleDelete(report.id)"
          >
            {{ isDeleting === report.id ? 'Deleting...' : 'Delete' }}
          </button>

          <button
            class="block-button"
            @click="handleBlock(report.reported_user_id)"
          >
            Block
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.reports-container {
  padding: 1rem;
}

.error {
  color: red;
  margin: 1rem 0;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f4f4f4;
}

.delete-button {
  background-color: #d9534f;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:disabled {
  background-color: #bbb;
}
</style>
