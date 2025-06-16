import { apiFetch } from '@/services/api.ts'
import type { Comments } from '@/interface/Comments.ts'

/**
 * Retrieves all comments from the API.
 *
 * @return {Promise<Comments[]>} A promise that resolves to an array of comments.
 */
export async function getAllComments(): Promise<Comments[]> {
  return await apiFetch<Comments[]>('/comments');
}

/**
 * Creates a new comment by sending data to the server.
 *
 * @param {Comments} comment - The comment object to create, containing necessary details.
 * @return {Promise<Comments>} A promise that resolves to the created comment object returned by the server.
 */
export async function createComment(comment: Comments): Promise<Comments> {
  return await apiFetch<Comments>('/comments', {
    method: 'POST',
    body: JSON.stringify(comment),
  });
}

/**
 * Retrieves a comment by its unique identifier.
 *
 * @param {number} id - The unique identifier of the comment to retrieve.
 * @return {Promise<Comments>} A promise that resolves to the comment data.
 */
export async function getCommentById(id: number): Promise<Comments> {
  return await apiFetch<Comments>(`/comments/${id}`);
}

/**
 *
 */
export async function getCommentByPostId(postsId: number): Promise<Comments[]> {
  return await apiFetch<Comments[]>(`/comments/posts/${postsId}`);
}

/**
 * Fetches comments associated with a specific user based on the provided user ID.
 *
 * @param {number} userId - The unique identifier of the user whose comments are to be retrieved.
 * @return {Promise<Comments[]>} A promise that resolves to an array of comments belonging to the specified user.
 */
export async function getCommentByUserId(userId: number): Promise<Comments[]> {
  return await apiFetch<Comments[]>(`/comments/users/${userId}`);
}

/**
 * Fetches comments based on the provided comments ID.
 *
 * @param {number} commentsId - The unique identifier of the comments to retrieve.
 * @return {Promise<Comments[]>} A promise that resolves to an array of comments.
 */
export async function getCommentByCommentsId(commentsId: number): Promise<Comments[]> {
  return await apiFetch<Comments[]>(`/comments/commentsId/${commentsId}`);
}

/**
 * Deletes a comment with the specified ID.
 *
 * @param {number} id - The unique identifier of the comment to delete.
 * @return {Promise<void>} A promise that resolves when the comment is successfully deleted.
 */
export async function deleteComment(id: number): Promise<void> {
  await apiFetch(`/comments/${id}`, {
    method: 'DELETE',
  })
}
