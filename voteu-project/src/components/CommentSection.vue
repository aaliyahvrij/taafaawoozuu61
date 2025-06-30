<script setup lang="ts">
import { ref, computed } from 'vue'
import { getCommentByPostId, createComment } from '@/services/CommentsService'
import type { Comments } from '@/interface/Comments'

const props = defineProps<{ postId: number }>()
const comments = ref<Comments[]>([])
const newComment = ref('')
const user = JSON.parse(localStorage.getItem('loggedInUser') || '{}')

const fetchComments = async () => {
  comments.value = await getCommentByPostId(props.postId)
}


const topLevelComments = computed(() =>
  comments.value.filter((c) => !c.commentsId || c.commentsId === 0)
)

const getUsername = (userId: number) => `User ${userId}`

const submitComment = async () => {
  if (!newComment.value.trim()) return;

  if (!user || !user.id) {
    alert(' Please login to post a comment.');
    return;
  }

  const commentData = {
    body: newComment.value,
    postsId: { id: props.postId },
    userId: { id: user.id },
    commentsId: 0,
    createdAt: new Date().toISOString()
  }

  try {
    await createComment(commentData as never);
    newComment.value = '';
    await fetchComments();
  } catch (error) {
    console.error('wrong by sending a comment:', error);
    alert('something wrong by sending a comment, please try again later.');
  }
};
</script>

<template>
  <div class="comment-section">
    <h4>Replies</h4>

    <div v-for="comment in topLevelComments" :key="comment.id" class="comment">
      <p><strong>{{ getUsername(comment.userId) }}</strong>: {{ comment.body }}</p>

      <!-- Replies -->
      <div class="replies" v-if="comment.replies.length">
        <div v-for="reply in comment.replies" :key="reply.id" class="reply">
          <p><strong>{{ getUsername(reply.userId) }}</strong>: {{ reply.body }}</p>
        </div>
      </div>
    </div>

    <!-- new reaction -->
    <form @submit.prevent="submitComment">
      <input v-model="newComment" placeholder="Typ a reply..." />
      <button type="submit">submit</button>
    </form>
  </div>
</template>

<style scoped>
.comment-section {
  margin-top: 1rem;
}
.comment {
  border-bottom: 1px solid #ccc;
  padding: 0.5rem 0;
}
.reply {
  margin-left: 1.5rem;
  font-style: italic;
}
</style>
