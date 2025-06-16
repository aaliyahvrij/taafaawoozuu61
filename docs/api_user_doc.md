# REST API Documentation

## Base URL

```
http://localhost:8080/api
```

---

## Authentication

### POST `/auth/login`

**Description**: Authenticates a user and returns a JWT token.

**Request Body**:

```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

**Status Codes**:

* `200 OK` – Successfully authenticated
* `401 Unauthorized` – Invalid credentials

---

## Users

### GET `/users`

**Description**: Returns a list of all users.
**Access**: Public or Admin (depending on your actual security config).

**Response**:

```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
]
```

---

### GET `/users/id/{id}`

**Description**: Get a single user by ID.

**Path Variable**:

* `id`: ID of the user

**Response**:

```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

**Status Codes**:

* `200 OK` – Found
* `404 Not Found` – User not found

---

### POST `/users`

**Description**: Register a new user. And defaults all registers with the role 'USER'

**Request Body**:

```json
{
  "username": "janedoe",
  "email": "jane@example.com",
  "password": "mypassword", 
  "firstName": "jane",
  "lastName": "doe",
  "gender": "other",
  "country": "NL",
  "createdAt": "2025-06-04T13:05:58Z"
}
```

**Response**:

```json
{
  "id": 2,
  "role": "USER",
  "username": "jane",
  "email": "jane@example.com",
  "firstName": "Jane",
  "lastName": "Doe",
  "gender": "female",
  "country": "NL",
  "password": "1234",
  "createdAt": "2025-06-04T13:05:58Z"
}
```

---

### DELETE `/users/id/{id}`

**Description**: Deletes a user by ID.
**Access**: Admin only
**Authorization**: `Bearer <JWT_TOKEN>`

**Status Codes**:

* `204 No Content` – User successfully deleted
* `403 Forbidden` – Not authorized
* `404 Not Found` – User does not exist

---

## Countries

### GET `/countries`

**Description**: Get a list of all countries.
**Access**: Public

**Response**:

```json
[
  {
    "id": 1,
    "name": "Germany",
    "code": "DE"
  }
]
```

---

## Admin Routes

### GET `/admin`

**Description**: Admin dashboard route
**Access**: Requires `ROLE_ADMIN`
**Authorization**: `Bearer <JWT_TOKEN>`

---

# Authorization Header

Send the JWT in the header for protected routes:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR...
```

---
