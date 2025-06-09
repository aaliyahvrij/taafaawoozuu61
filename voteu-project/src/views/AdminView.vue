<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getAllUsers, deleteUser } from '@/services/UserService.ts';
import type { User } from '@/interface/User.ts';

const users = ref<User[]>([]); // Reactive array to hold users
const error = ref<string | null>(null); // To handle errors
const isLoading = ref<boolean>(true); // Loading state
const isDeleting = ref<number | null>(null); // Tracks which user is being deleted

// Function to fetch all users
const fetchUsers = async () => {
  try {
    users.value = await getAllUsers(); // Call getAllUsers() from UserService.ts
    error.value = null;
  } catch (err) {
    console.error('Error fetching users:', err);
    error.value = 'Unable to fetch users. Please try again later.';
  } finally {
    isLoading.value = false;
  }
};

// Function to delete a user
const handleDelete = async (userId: number) => {
  if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
    return;
  }

  isDeleting.value = userId; // Set the deleting state
  try {
    await deleteUser(userId); // Call deleteUser() from UserService.ts
    // Remove the user from the local list after deletion
    users.value = users.value.filter((user) => user.id !== userId);
    alert('User successfully deleted.');
  } catch (err) {
    console.error('Error deleting user:', err);
    alert('An error occurred while deleting the user. Please try again.');
  } finally {
    isDeleting.value = null; // Reset the deleting state
  }
};

// Fetch users on component load
onMounted(() => {
  fetchUsers();
});
</script>

<template>
  <div class="users-container">
    <h2>All Users</h2>

    <!-- Loading Indicator -->
    <div v-if="isLoading" class="loading">Loading users, please wait...</div>

    <!-- Error Message -->
    <div v-if="error" class="error">{{ error }}</div>

    <!-- Table Content -->
    <table v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Gender</th>
          <th>Country</th>
          <th>Created At</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <!-- Dynamically Iterate Over Users -->
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.firstName }}</td>
          <td>{{ user.lastName }}</td>
          <td>{{ user.gender }}</td>
          <td>{{ user.country }}</td>
          <td>{{ new Date(user.createdAt || '').toLocaleString() }}</td>
          <td>
            <!-- Action Buttons -->
            <button
              class="delete-button"
              :disabled="isDeleting === user.id"
              @click="handleDelete(user.id!)"
            >
              {{ isDeleting === user.id ? 'Deleting...' : 'Delete' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.users-container {
  padding: 1rem;
}

.loading,
.error {
  font-size: 1rem;
  color: red;
  margin: 1rem 0;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

table th,
table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.delete-button {
  padding: 4px 8px;
  border: none;
  background-color: #d9534f;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:disabled {
  background-color: #c0c0c0;
  cursor: not-allowed;
}

.delete-button:hover:not(:disabled) {
  background-color: #c9302c;
}
</style>
