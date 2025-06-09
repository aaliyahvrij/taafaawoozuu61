import { apiFetch } from '@/services/api.ts'
import type { User } from '@/interface/User.ts'

/**
 * Fetches and returns a list of all users.
 *
 * @return {Promise<User[]>} A promise that resolves to an array of User objects.
 */
export async function getAllUsers(): Promise<User[]> {
  return await apiFetch<User[]>('/users');
}

/**
 * Fetches a user by their unique identifier.
 *
 * @param {number} id - The unique identifier of the user to retrieve.
 * @return {Promise<User>} A promise that resolves with the user object.
 */
export async function getUserById(id: number): Promise<User> {
  return await apiFetch<User>(`/users/${id}`);
}

/**
 * Fetches and returns a user by their email address.
 *
 * @param {string} email - The email address of the user to retrieve.
 * @return {Promise<User>} A promise that resolves to the user object.
 */
export async function getUserByEmail(email: string): Promise<User> {
  return await apiFetch<User>(`/users/email/${email}`);
}

/**
 * Retrieves a user by their username.
 *
 * @param {string} username - The username of the user to retrieve.
 * @return {Promise<User>} A promise that resolves to the user object matching the given username.
 */
export async function getUserByUsername(username: string): Promise<User> {
  return await apiFetch<User>(`/users/username/${username}`);
}

/**
 * Creates a new user by sending the provided user details to the API.
 *
 * @param {User} user - The user object containing details of the user to be created.
 * @return {Promise<User>} A promise that resolves to the created user object.
 */
export async function createUser(user: User): Promise<User> {
  return await apiFetch<User>('/users', {
    method: 'POST',
    body: JSON.stringify(user),
  });
}

/**
 * Updates the user information on the server.
 *
 * @param {User} user - An object representing the user to be updated, including its unique identifier and updated properties.
 * @return {Promise<User>} A promise that resolves to the updated user object returned from the server.
 */
export async function updateUser(user: User): Promise<User> {
  return await apiFetch<User>(`/users/${user.id}`, {
    method: 'PUT',
  })
}

/**
 * Deletes a user identified by the given ID.
 *
 * Sends a DELETE request to remove the user associated with the specified ID.
 *
 * @param {number} id - The unique identifier of the user to be deleted.
 * @return {Promise<void>} A promise that resolves when the user is successfully deleted.
 */
export async function deleteUser(id: number): Promise<void> {
  await apiFetch(`/users/${id}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`, // Replace 'localStorage.getItem' as needed in your setup
      'Content-Type': 'application/json',
    },

  })
}

