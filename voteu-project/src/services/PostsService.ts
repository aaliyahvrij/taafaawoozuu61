import { apiFetch } from '@/services/api.ts'
import type { Posts } from '@/interface/Posts.ts'

/**
 * Fetches all posts from the API.
 *
 * @return {Promise<Posts[]>} A promise that resolves to an array of posts.
 */
export async function getAllPosts(): Promise<Posts[]> {
  return await apiFetch<Posts[]>('/posts');
}

/**
 * Creates a new post by sending a POST request to the specified endpoint.
 *
 * @param {Posts} post - The post object containing the necessary data to create a new post.
 * @return {Promise<Posts>} A promise that resolves to the created post object.
 */
export async function createPost(post: Posts): Promise<Posts> {
  return await apiFetch<Posts>('/posts', {
    method: 'POST',
    body: JSON.stringify(post),
  });
}

/**
 * Fetches a post by its unique identifier.
 *
 * @param id The unique identifier of the post to retrieve.
 * @return A promise that resolves to the post object corresponding to the given ID.
 */
export async function getPostById(id: number): Promise<Posts> {
  return await apiFetch<Posts>(`/posts/${id}`);
}

/**
 * Deletes a post by its unique identifier (ID).
 *
 * @param {number} id - The unique identifier of the post to delete.
 * @return {Promise<void>} A promise that resolves when the post has been deleted.
 */
export async function deletePost(id: number): Promise<void> {
  await apiFetch(`/posts/${id}`, {
    method: 'DELETE',
  })
}
