<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPostById } from '@/services/PostsService.ts'
import { useRoute } from 'vue-router'
import type { Posts } from '@/interface/Posts.ts'
import type { User } from '@/interface/User.ts'
import { authService } from '@/services/AuthService.ts'
import { getUserByUsername } from '@/services/UserService.ts'
import { deletePost } from '@/services/PostsService.ts'
import router from '@/router'

const  route = useRoute()

const post = ref<Posts>({
  id:0,
  title: '',
  description: '',
  body: '',
  createdAt: '',
  comments: [],
  user: {
    id: 0,
    username: '',
  }
})
const user = ref<User | null>(null)
const error = ref<string | null>(null)
const fetchUserProfile = async () => {
  try {
    const decodedToken = authService.getDecodedToken()
    if (decodedToken && decodedToken.sub) {

      user.value = await getUserByUsername(decodedToken.sub)
      console.log(user.value.id)
    } else {
      error.value = 'Failed to retrieve user information. Please log in again.'
    }
  } catch (err) {
    error.value = 'Failed to fetch user profile'
    console.error(err)
  }
}
onMounted(fetchUserProfile)

onMounted(async function(){
  try{
    const postId : number = Number(route.params.id)
    const response = await getPostById(postId)
    post.value.id = response.id
    post.value.title = response.title;
    post.value.description = response.description;
    post.value.body = response.body;
    post.value.createdAt = response.createdAt;
    post.value.comments = response.comments;
    post.value.user.username = response.user.username
    post.value.user.id = response.user.id
  }
  catch (e) {
    console.log(e)
  }
})



async function handleDeletePost(id:number) :Promise<void> {
  const confirmed = confirm('Are you sure you want to delete this post?')
  if (!confirmed) return
  try{
    await deletePost(id)
    router.push({ path: `/forum` });
    console.log(`Post with ID ${id} deleted`)
  } catch (e){
    console.log(e)
  }
}



</script>

<template>
  <div class="page-container">
    <div class="content-wrapper">
      <div class="card">
        <div class="header">
          <h1>Post</h1>
        </div>

        <div class="post">
          <p class="post-title">{{post.title}}</p>
          <hr />
          <p class="post-author">Created by <b>{{post.user.username}}</b></p>
          <p>{{post.description}}</p>

          <div class="post-body">
            <div class="post-content">
              <p class="post-text">{{post.body}}</p>
            </div>

            <div class="actions-row">


              <button
                class="delete-button"
                v-if="user && user.id === post.user.id"
                @click="handleDeletePost(post.id)" aria-label="Delete Post">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  height="24px"
                  viewBox="0 -960 960 960"
                  width="24px"
                  fill="#ffffff"
                >
                  <path
                    d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content-wrapper {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.card {
  background-color: #f9fafb;
  margin: 2.5rem;
  border: 1px solid #ccc;
  padding: 3rem 1.5rem;
  max-width: 768px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.header {
  margin-bottom: 2rem;
  text-align: center;
}

.header h2 {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1a1a1a;
}

.post {
  width: 100%;
}

.post-title {
  font-size: 1.5rem;
  font-weight: 500;
  color: #1a1a1a;
  margin-bottom: 1rem;
}

.post-author b {
  margin: 1rem 0;
  color: #1e40af;
}

.post-body {
  display: flex;
  flex-direction: column;
}

.post-content {
  border-radius: 0.5rem;
  border: 1px solid #d1d5db;
  height: auto;
  padding: 5px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

/* NEW: container to align like + delete horizontally */
.actions-row {
  display: flex;
  align-items: center;
  margin-top: 1rem;
  gap: 1rem; /* space between like and delete */
}



.delete-button {
  background-color: #000;
  color: white;
  padding: 0.5rem;
  border: 2px solid #000;
  border-radius: 0.25rem;
  width: 60px;
  cursor: pointer;
  /* remove float, margin-left */
  float: none;
  margin-left: 0;
}
</style>
