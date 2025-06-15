<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getAllCountries } from '@/services/CountriesService.ts'
import type { Countries } from '@/interface/Countries.ts'
import type { UpdateUser, User } from '@/interface/User.ts'
import { getUserByUsername, updateUser } from '@/services/UserService.ts'
import { authService } from '@/services/AuthService.ts'

const user = ref<User | null>(null)
const countries = ref<Countries[]>([])
const error = ref<string | null>(null)
  const successMessage = ref<string | null>(null)

    const fetchUserProfile = async () => {
    try {
    const decodedToken = authService.getDecodedToken()
    if (decodedToken && decodedToken.sub) {

    user.value = await getUserByUsername(decodedToken.sub)
    } else {
    error.value = 'Failed to retrieve user information. Please log in again.'
    }
    } catch (err) {
    error.value = 'Failed to fetch user profile'
    console.error(err)
    }
    }

    onMounted(fetchUserProfile)

    onMounted(async () => {
    try {
    countries.value = await getAllCountries()
    } catch (e) {
    error.value = 'Failed to fetch countries'
    console.error(e)
    }
    })

    const updateProfile = async (updatedData: UpdateUser) => {
    try {
    if (!user.value?.id) return

    user.value = await updateUser({
    id: user.value.id,
    ...updatedData,
    })
    successMessage.value = 'Profile updated successfully!, please logout and login again to see the changes.'
    error.value = null
    } catch (err) {
    error.value = 'Failed to update profile'
    console.error(err)
    }
    }
    </script>

    <template>
      <div class="profile-container">
        <div v-if="user" class="profile-header">
          <div class="profile-info">
            <h2 id="profile-username">{{ user.username }}</h2>
            <p id="profile-email">{{ user.email }}</p>
          </div>
          <button class="edit-btn" @click="fetchUserProfile">Refresh</button>
        </div>

        <div v-if="error" class="error-message">
          <p>{{ error }}</p>
        </div>

        <div v-if="successMessage" class="success-message">
          <p>{{ successMessage }}</p>
        </div>

        <form @submit.prevent="updateProfile(user)" class="profile-form" v-if="user">
          <div class="form-grid">
            <div class="form-group">
              <label>Username</label>
              <input type="text" v-model="user.username" placeholder="Your Username" />
            </div>
            <div class="form-group">
              <label>First Name</label>
              <input type="text" v-model="user.firstName" placeholder="Your First Name" />
            </div>
            <div class="form-group">
              <label>Last Name</label>
              <input type="text" v-model="user.lastName" placeholder="Your Last Name" />
            </div>
            <div class="form-group">
              <label>Country</label>
              <select v-model="user.country">
                <option disabled value="">Select your country</option>
                <option v-for="country in countries" :key="country.id" :value="country.code">
                  {{ country.name }}
                </option>
              </select>
            </div>
          </div>
          <button type="submit" class="submit-btn">Update Profile</button>
        </form>

        <div v-else class="loading-message">
          <p>Loading user profile...</p>
        </div>
      </div>
    </template>

<style scoped>
.error-message {
  color: red;
  margin-bottom: 1rem;
}
.success-message {
  color: green;
  margin-bottom: 1rem;
}
.submit-btn {
  margin-top: 1rem;
  background-color: #0a2342;
  color: white;
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.profile-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Segoe UI', sans-serif;
}

.profile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.profile-info {
  flex: 1;
  margin-left: 1.5rem;
}

.profile-info h2 {
  margin: 0;
  font-size: 1.5rem;
}

.profile-info p {
  color: gray;
}

.edit-btn {
  background-color: #0a2342;
  color: white;
  padding: 0.5rem 1.25rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.profile-form {
  margin-top: 2rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group select {
  padding: 0.75rem;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #fafafa;
}

.email-section h3 {
  margin-bottom: 1rem;
}

.email-info p {
  margin: 0;
  font-weight: 500;
}

.email-info small {
  color: gray;
}
</style>
