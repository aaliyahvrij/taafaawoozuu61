<template>
  <div class="forum-container">
    <h1 class="forum-title">Election Forum</h1>

    <!-- Add Comment button (top-right corner) -->
    <div class="top-bar">
      <button class="add-comment-btn">Add Comment</button>
    </div>

    <!-- Warning if user not logged in -->
    <div class="login-warning">
      <p>You must be <a href="/login">logged in</a> to comment.</p>
    </div>

    <!-- Comment form (only show if user is logged in and clicked "Add Comment") -->
    <div class="comment-form">
      <textarea placeholder="Write your comment..." class="textarea"></textarea>
      <button class="submit-btn">Post Comment</button>
    </div>

    <!-- Comment box (like a todo list) -->
    <div class="comments-box">
      <h2 class="comments-title">All Comments</h2>
      <ul class="comment-list">
        <li class="comment-item">
          <span class="author">Lisa:</span> I think every vote matters.
        </li>
        <li class="comment-item">
          <span class="author">Ahmed:</span> The system needs to be more transparent.
        </li>
        <!-- Add more items here -->
      </ul>
    </div>
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

.add-comment-btn {
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  font-size: 0.95rem;
  border-radius: 0.5rem;
  cursor: pointer;
}

.add-comment-btn:hover {
  background-color: #2563eb;
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

.comment-form {
  background-color: #e5e7eb;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 2rem;
  display: none; /* default hidden, show via script when Add Comment is clicked */
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
  background-color: #10b981;
  color: white;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #059669;
}

.comments-box {
  background-color: #ffffff;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  padding: 1.5rem;
}

.comments-title {
  font-size: 1.25rem;
  margin-bottom: 1rem;
  color: #111827;
}

.comment-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.comment-item {
  background-color: #f3f4f6;
  border-radius: 0.5rem;
  padding: 0.75rem;
  margin-bottom: 0.75rem;
  font-size: 1rem;
  color: #374151;
}

.comment-item .author {
  font-weight: bold;
  color: #1e40af;
}
</style>
