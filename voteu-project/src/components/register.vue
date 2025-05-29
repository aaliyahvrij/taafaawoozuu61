<script setup lang="ts">

</script>

<template>
<div>
  <!-- Username Field -->
  <div>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" />
  </div>

  <!-- Email Field -->
  <div>
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" />
  </div>

  <!-- First Name Field -->
  <div>
    <label for="first-name">First Name:</label>
    <input type="text" id="first-name" name="first-name" />
  </div>

  <!-- Last Name Field -->
  <div>
    <label for="last-name">Last Name:</label>
    <input type="text" id="last-name" name="last-name" />
  </div>

  <!-- Gender Dropdown -->
  <div>
    <label for="gender">Gender:</label>
    <select id="gender" name="gender">
      <option value="female">Female</option>
      <option value="male">Male</option>
      <option value="other">Other</option>
    </select>
  </div>

  <div>
  <label for="country">Country:</label>
  <select id="country" name="country"></select>
</div>

  <div>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" />
  </div>

  <button type="submit">Register</button>
</div>
</template>

<style scoped>

</style>
