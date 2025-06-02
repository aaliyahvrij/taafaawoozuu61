<script setup lang="ts">
</script>

<template>
  <div class="login-container">
    <div class="circle-bg">
      <form class="form">
        <h2 class="form-title">Login</h2>

        <div class="form-group">
          <label for="email">Email address</label>
          <input type="email" id="email" />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" />
        </div>

        <div class="form-group checkbox-group">
          <input type="checkbox" id="remember" />
          <label for="remember">Remember me</label>
        </div>

        <button class="submit-btn" type="submit">Sign In</button>

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

input[type="email"],
input[type="password"] {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.checkbox-group {
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
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
