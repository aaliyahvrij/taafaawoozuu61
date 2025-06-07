import { ref } from 'vue'
import type { User } from '@/interface/User.ts'

const user = ref<User | null>(null)

const storedUser = localStorage.getItem('loggedInUser')
if (storedUser) {
  user.value = JSON.parse(storedUser)
}

function login(newUser: User) {
  user.value = newUser
  localStorage.setItem('loggedInUser', JSON.stringify(newUser))
}

function logout() {
  user.value = null
  localStorage.removeItem('loggedInUser')
}

function isLoggedIn() {
  return !!user.value
}


export function useAuth() {
  return {
    user,
    login,
    logout,
    isLoggedIn,

  }
}
