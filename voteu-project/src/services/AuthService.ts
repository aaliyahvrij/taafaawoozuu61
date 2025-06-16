import { jwtDecode } from 'jwt-decode'
import { apiFetch } from '@/services/api.ts'

interface DecodedToken {
  sub: string;
  role: string;
  exp: number;
  iat: number;
}

const TOKEN_KEY = 'token';

export const authService = {
  login: async (username: string, password: string) => {
    const data = await apiFetch<{ token: string }>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });

    localStorage.setItem(TOKEN_KEY, data.token);
  },

  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
  },

  getToken: () => localStorage.getItem(TOKEN_KEY),

  getDecodedToken: (): DecodedToken | null => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) return null;
    return jwtDecode(token);
  },

  isAuthenticated: (): boolean => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) return false;

    const decoded: DecodedToken = jwtDecode(token);
    return decoded.exp * 1000 > Date.now();
  },

  getUserRole: (): string | null => {
    const decoded = authService.getDecodedToken();
    return decoded?.role ?? null;
  }
};
