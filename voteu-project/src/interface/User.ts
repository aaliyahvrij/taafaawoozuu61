export interface User {
  id?: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  gender: string;
  country: string;
  password: string;
  createdAt?: string;
  isBlocked?: boolean;
}

export type UpdateUser = Partial<User>;

