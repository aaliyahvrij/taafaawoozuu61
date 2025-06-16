<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPostById } from '@/services/PostsService.ts'
import { useRoute } from 'vue-router'
import type { Posts } from '@/interface/Posts.ts'

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

onMounted(async function(){
  try{
    const postId : number = Number(route.params.id)
    const response = await getPostById(postId)
    console.log(response)
    post.value.title = response.title;
    post.value.description = response.description;
    post.value.body = response.body;
    post.value.createdAt = response.createdAt;
    post.value.comments = response.comments;
    post.value.user.username = response.user.username
  }
  catch (e) {
    console.log(e)
  }
})

const showDelete = ref(true)
const postdata = ref({ likes: [] })

function handleAddLike() {
  console.log('Like added')
}

function handleDeletePost() {
  console.log(`Post with ID ${post.value.id} deleted`)
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
              <div class="like-section">
                <button @click="handleAddLike" class="like-button">
                  <svg
                    class="like-icon"
                    xmlns="http://www.w3.org/2000/svg"
                    height="24px"
                    viewBox="0 -960 960 960"
                    width="24px"
                    fill="#000000"
                  >
                    <path
                      d="M720-120H280v-520l280-280 50 50q7 7 11.5 19t4.5 23v14l-44 174h258q32 0 56 24t24 56v80q0 7-2 15t-4 15L794-168q-9 20-30 34t-44 14Zm-360-80h360l120-280v-80H480l54-220-174 174v406Zm0-406v406-406Zm-80-34v80H160v360h120v80H80v-520h200Z"
                    />
                  </svg>
                </button>

                <div class="like-count">{{ postdata.likes.length }}</div>
              </div>

              <button class="delete-button" v-show="showDelete" @click="handleDeletePost" aria-label="Delete Post">
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

.like-section {
  display: flex;
  align-items: center;
}

.like-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.like-icon {
  transition: transform 0.2s ease-in-out;
}

.like-icon:hover {
  transform: scale(1.25);
}

.like-count {
  margin-left: 0.5rem;
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
