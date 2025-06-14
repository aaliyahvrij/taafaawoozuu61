# PostsController API Documentation

This controller handles CRUD operations related to **Posts** within the VoteU Election system.

##  Base URL

```
/api/posts
```

---

##  Endpoints

###  Get All Posts

**Request**

```
GET /api/posts
```

**Response**

- `200 OK` — Returns a list of all posts.

**Example Response**

```json
[
  {
    "id": 1,
    "title": "Election Updates",
    "content": "Latest results are in..."
  }
]
```

---

###  Create a New Post

**Request**

```
POST /api/posts
```

**Body**

```json
{
  "title": "New Announcement",
  "content": "Elections will be held next month"
}
```

**Response**

- `200 OK` — Returns the newly created post-object.

**Example Response**

```json
{
  "id": 2,
  "title": "New Announcement",
  "content": "Elections will be held next month"
}
```

---

###  Get Post by ID

**Request**

```
GET /api/posts/{id}
```

**Path Parameter**

- `id` — The unique identifier of the post.

**Response**

- `200 OK` — Returns the post with the given ID.
- `404 Not Found` — If no post exists with the provided ID.

**Example Response**

```json
{
  "id": 1,
  "title": "Election Results",
  "content": "Official results are announced"
}
```

---

###  Delete Post

**Request**

```
DELETE /api/posts/{id}
```

**Path Parameter**

- `id` — The unique identifier of the post to delete.

**Response**

- `204 No Content` — Post deleted successfully.

---

##  Exceptions

- **`ResourceNotFoundException`** — Thrown when ID does not find a requested post.

---

##  Notes

- The controller uses `PostsService` for business logic and data access.
- All endpoints return standard HTTP status codes.
- JSON is used for both request and response formats.