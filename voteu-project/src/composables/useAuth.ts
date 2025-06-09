import { ref } from 'vue';
import { authService } from '@/services/AuthService.ts'


const isLoggedIn = ref(authService.isAuthenticated());

export function useAuth() {
  const login = async (username: string, password: string) => {
    await authService.login(username, password);
    isLoggedIn.value = true;
  };

  const logout = () => {
    authService.logout();
    isLoggedIn.value = false;
  };

  return {
    isLoggedIn,
    login,
    logout,
    getRole: authService.getUserRole,
  };
}
