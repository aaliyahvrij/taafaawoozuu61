
<script setup lang="ts">
</script>

<template>
  <div class="register-container">
    <div class="circle-bg">
      <form class="form">
        <h2 class="form-title">Register</h2>

        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" name="username" />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" />
        </div>

        <div class="form-group">
          <label for="first-name">First Name</label>
          <input type="text" id="first-name" name="first-name" />
        </div>

        <div class="form-group">
          <label for="last-name">Last Name</label>
          <input type="text" id="last-name" name="last-name" />
        </div>

        <div class="form-group">
          <label for="gender">Gender</label>
          <select id="gender" name="gender">
            <option value="female">Female</option>
            <option value="male">Male</option>
            <option value="other">Other</option>
          </select>
        </div>

        <div class="form-group">
          <label for="country">Country</label>
          <select id="country" name="country">
            <option value="">Select your country</option>
          </select>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" />
        </div>

        <button class="submit-btn" type="submit">Register</button>
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

