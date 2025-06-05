<script setup lang="ts">

import { onMounted, ref } from 'vue'
import type { Countries } from '@/interface/Countries.ts'
import { getAllCountries } from '@/services/CountriesService.ts'
import { createUser } from '@/services/UserService.ts'

const countries = ref<Countries[]>([]);
const error = ref<string | null>(null);
const emailRegex: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const registerForm = ref({
  username: '',
  email: '',
  firstName: '',
  lastName: '',
  gender: '',
  country: '',
  password: '',
})

// TODO: make html and css of this so the user sees it
function validatePassword(password: string): string | null {
  if (password.length < 8) {
    return 'Password must be at least 8 characters long.';
  }
  if (!/[A-Z]/.test(password)) {
    return 'Password must contain at least one uppercase letter.';
  }
  if (!/[a-z]/.test(password)) {
    return 'Password must contain at least one lowercase letter.';
  }
  if (!/\d/.test(password)) {
    return 'Password must contain at least one number.';
  }
  if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
    return 'Password must contain at least one special character.';
  }
  return null;
}

function validateForm(): boolean {
  const { password, email } = registerForm.value;

  const passwordError = validatePassword(password);
  if (passwordError) {
    error.value = passwordError;
    return false;
  }

  if (!emailRegex.test(email)) {
    error.value = 'Please enter a valid email address.';
    return false;
  }

  error.value = null;
  return true;
}

const emit = defineEmits(['submit'])

const submit = async () => {

  if (!validateForm()) return;
  emit('submit', { ...registerForm.value });

  try {
    const newUser = await createUser(registerForm.value);
    console.log('User created:', newUser);
    emit('submit', newUser);
  } catch (e) {
    error.value = 'Failed to create user';
    console.error(e);
  }
}

onMounted(async () => {
  try {
    countries.value = await getAllCountries();
  } catch (e) {
    error.value = 'Failed to fetch countries';
    console.error(e);
  }
});


</script>

<template>
  <div class="register-container">
    <div class="circle-bg">
      <form class="form">
        <h2 class="form-title">Register</h2>

        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" name="username" v-model="registerForm.username" />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" v-model="registerForm.email"/>
        </div>

        <div class="form-group">
          <label for="first-name">First Name</label>
          <input type="text" id="first-name" name="first-name" v-model="registerForm.firstName"/>
        </div>

        <div class="form-group">
          <label for="last-name">Last Name</label>
          <input type="text" id="last-name" name="last-name" v-model="registerForm.lastName"/>
        </div>

        <div class="form-group">
          <label for="gender">Gender</label>
          <select id="gender" name="gender" v-model="registerForm.gender">
            <option value="female">Female</option>
            <option value="male">Male</option>
            <option value="other">Other</option>
          </select>
        </div>

        <div class="form-group">
          <label for="country">Country</label>
          <select id="country" name="country" v-model="registerForm.country">
            <option disabled value="">Select your country</option>
            <option
              v-for="country in countries"
              :key="country.id"
              :value="country.code"
            >
              {{ country.name }}
            </option>
          </select>
        </div>


        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" v-model="registerForm.password"/>
        </div>

        <button class="submit-btn" @click.prevent="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  box-sizing: border-box;
}

.circle-bg {
  background-color: #fde4c3;
  border-radius: 50%;
  width: 800px;   /* Groter dan eerst */
  height: 800px;  /* Even groot voor een perfecte cirkel */
  display: flex;
  justify-content: center;
  align-items: center;
}

.form {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.form-title {
  text-align: center;
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}

label {
  font-weight: 500;
  margin-bottom: 0.3rem;
}

input,
select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 0.95rem;
}

.submit-btn {
  margin-top: 1rem;
  background-color: #002b80;
  color: white;
  padding: 0.7rem;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-btn:hover {
  background-color: #0040ff;
}
</style>

