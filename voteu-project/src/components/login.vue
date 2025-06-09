<script setup lang="ts">

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/AuthService.ts'


const error = ref<string | null>(null);
const loginForm = ref({
  username: '',
  password: '',
})


const router = useRouter()

const emit = defineEmits(['submit'])

const submit = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    error.value = 'Please enter both username and password.';
    return;
  }

  try {

    await authService.login(loginForm.value.username, loginForm.value.password);

    emit('submit', authService.getDecodedToken());

    error.value = null;
    console.log('User logged in:', authService.getDecodedToken());

    await router.push('/');

    const role = authService.getUserRole();

    if (role === 'ADMIN') {
      router.push('/admin');
    } else {
      router.push('/home');
    }

  } catch (err) {
    console.error(err);
    error.value = 'Invalid username or password.';
  }
};


</script>

<template>
  <div class="login-container">
    <div class="circle-bg">
      <form class="form">
        <h2 class="form-title">Login</h2>

        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="loginForm.username"/>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="loginForm.password"/>
        </div>

        <div v-if="error" class="error-message">{{ error }}</div>

        <button class="submit-btn" @submit.prevent="submit">Sign In</button>

        <p class="link-text">
          Not a member? <RouterLink to="/register">Create an account</RouterLink>
        </p>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
}

.circle-bg {
  background-color: #fde4c3;
  border-radius: 50%;
  width: 500px;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.form {
  display: flex;
  flex-direction: column;
  width: 80%;
}

.form-title {
  text-align: center;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}

input[type="text"],
input[type="password"] {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}



.submit-btn {
  background-color: #002b80;
  color: white;
  padding: 0.7rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  margin-top: 1rem;
}

.submit-btn:hover {
  background-color: #0040ff;
}

.link-text {
  text-align: center;
  margin-top: 1rem;
}
</style>
