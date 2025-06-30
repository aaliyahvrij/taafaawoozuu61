<script lang="ts" setup>
import {computed, onMounted, ref} from 'vue'
import { useAuth } from '@/composables/useAuth'
import {createPost, getAllPosts} from '@/services/PostsService.ts'
import type { Posts } from '@/interface/Posts.ts'
import CommentSection from '@/components/CommentSection.vue'
import { useRouter } from 'vue-router'
import type { User } from '@/interface/User.ts'
import { authService } from '@/services/AuthService.ts'
import { getUserByUsername } from '@/services/UserService.ts'
import ReportModal from '@/components/Data/user/ReportModal.vue'

const user = ref<User | null>(null)
const error = ref<string | null>(null)
const fetchUserProfile = async () => {
  try {
    const decodedToken = authService.getDecodedToken()
    if (decodedToken && decodedToken.sub) {

      user.value = await getUserByUsername(decodedToken.sub)
    } else {
      error.value = 'Failed to retrieve user information. Please log in again.'
    }
  } catch (err) {
    error.value = 'Failed to fetch user profile'
    console.error(err)
  }
}
onMounted(fetchUserProfile)

const router = useRouter()
const { isLoggedIn  } = useAuth()

const showForm = ref(false)
const posts = ref<Posts[]>([])

const showReportModal = ref(false)
const selectedPost = ref<Posts | null>(null)

onMounted(() => {
  loadPosts()
})

async function loadPosts() {
  posts.value = await getAllPosts()
}

const newPost = ref({
  title: '',
  description: '',
  body: ''
})

function handleAddPostClick() {
  showForm.value = !showForm.value
}

async function submitPost() {
  if (!newPost.value.title || !newPost.value.description || !newPost.value.body) {
    alert('Please fill in all fields.')
    return
  }

  if (!user.value?.id) {
    alert('Gebruiker niet ingelogd.');
    return;
  }

  try {
    await createPost({
      ...newPost.value,
      createdAt: new Date().toISOString(),
      user: { id: user.value.id, username: user.value.username  },
      comments: [],
      id: undefined as unknown as number
    })

    newPost.value = { title: '', description: '', body: '' }
    showForm.value = false
    await loadPosts()
  } catch (error) {
    console.error('Failed to submit post:', error)
    alert('Post submission failed.')
  }
}



function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  return date.toLocaleString('nl-NL', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })
}

function goToPost(postId : number) :void{
  console.log("ID:", postId);
  router.push({ path: `/post/${postId}` });
}

function openReportModal(post: Posts) {
  selectedPost.value = post
  console.log('Reporting user:', post.user)
  showReportModal.value = true
}
function handleReported() {
  window.alert('Report submitted')
}

const sortedPosts = computed(() => {
  return [...posts.value].sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
});

</script>

<template>
  <div class="forum-container">
    <h1 class="forum-title">Election Forum</h1>

    <div class="top-bar" v-if="isLoggedIn">
      <button class="add-post-btn" @click="handleAddPostClick">
        {{ showForm ? 'Cancel' : 'Add Post' }}
      </button>
    </div>

    <div class="login-warning" v-if="!isLoggedIn">
      <p>You must be <a href="/login">logged in</a> to add a post.</p>
    </div>

    <div class="post-form" v-if="isLoggedIn && showForm">
      <input
        v-model="newPost.title"
        placeholder="Post title"
        class="textarea"
        style="margin-bottom: 0.5rem; font-weight: bold;"
      />
      <input
        v-model="newPost.description"
        placeholder="Post description"
        class="textarea"
        style="margin-bottom: 0.5rem;"
      />
      <textarea
        placeholder="Write your post..."
        class="textarea"
        v-model="newPost.body"
      ></textarea>
      <button class="submit-btn" @click="submitPost">Post</button>
    </div>

    <div class="post-box ">
      <h2 class="post-title">All Posts</h2>
      <ul class="post-list">
        <li class="post-item" v-for="post in sortedPosts" :key="post.id">
          <div @click="goToPost(post.id)">
            <span class="author">👤{{ post.user.username }}</span>
            <hr>
            <strong>{{ post.title }}</strong><br />
            <em>{{ post.description }}</em><br />
            <small class="timestamp">📅 {{ formatDate(post.createdAt) }}</small>
          </div>

          <!-- Report knop -->
          <button class="report-btn" v-if="isLoggedIn && user?.id !== post.user.id" @click="openReportModal(post)">
            Report
          </button>

          <CommentSection v-if="post.id" :postId="post.id" />
        </li>
      </ul>
    </div>

    <!-- Report Modal -->
    <ReportModal
      v-if="user && user.id && selectedPost"
      :visible="showReportModal"
      :reporter="{ id: user.id, username: user.username }"
      :reported="{ id: selectedPost.user.id, username: selectedPost.user.username }"
      :post="{ id: selectedPost.id, title: selectedPost.title, body: selectedPost.body }"
      @close="showReportModal = false"
      @reported="handleReported"
    />
  </div>
</template>



<style scoped>
.forum-container {
  max-width: 800px;
  margin: auto;
  padding: 2rem;
  font-family: Arial, sans-serif;
  background-color: #f9fafb;
}

.forum-title {
  font-size: 2.5rem;
  font-weight: bold;
  text-align: center;
  color: #1f2937;
  margin-bottom: 1.5rem;
}

.top-bar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

.add-post-btn {
  border: none;
  padding: 0.6rem 1rem;
  font-size: 0.95rem;
  border-radius: 0.5rem;
  cursor: pointer;
}

.report-btn {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  background: none;
  border: none;
  color: #dc2626; /* rood */
  font-size: 0.85rem;
  cursor: pointer;
}

.report-btn:hover {
  text-decoration: underline;
}

.login-warning {
  background-color: #fff3cd;
  border: 1px solid #ffeeba;
  padding: 1rem;
  border-radius: 0.5rem;
  text-align: center;
  color: #856404;
  margin-bottom: 1.5rem;
}

.post-form {
  background-color: #e5e7eb;
  padding: 2rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
}

.textarea {
  width: 100%;
  padding: 0.75rem;
  font-size: 1rem;
  border-radius: 0.5rem;
  border: 1px solid #d1d5db;
  margin-bottom: 0.5rem;
}

.submit-btn {
  background-color: #1e40af;
  color: white;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #3b82f6;
}

.post-box {
  background-color: #ffffff;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  padding: 1.5rem;
}

.post-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #1f2937;
}

.post-list {
  list-style-type: none;
  margin: 0;
  max-height: 70vh;
  overflow-y: auto;
  padding: 0 0.5rem 0 0;
}

.post-item {
  background-color: #ffffff;
  border-radius: 0.75rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
  padding: 1rem;
  margin-bottom: 1rem;
  transition: background-color 0.3s;
  position: relative;
}
.post-item:hover {
  background-color: lightgray;
  cursor: pointer;
}

.post-item .author {
  font-weight: bold;
  color: #1d4ed8;
  display: inline-block;
  margin-bottom: 0.25rem;
}

.timestamp {
  display: block;
  margin-top: 0.5rem;
  color: #6b7280;
  font-size: 0.85rem;
}
</style>
