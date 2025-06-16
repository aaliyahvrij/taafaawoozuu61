
# REST API – ProvinceController

This REST API is for managing provinces in election years. It gives you endpoints to get province info, constituencies (regions in a province), and party votes.

## Base URL

```
/api/election/{year}/provinces
```

---

## Endpoints

### 1. Get All Provinces

**GET** `/api/election/{year}/provinces`

Returns all provinces for the given election year.

**Path Parameters**:
- `year`: Election year (like `"2023"`)

**Response**:  
A list of province objects.

```json
[
  { "id": 1, "name": "North Holland" },
  { "id": 2, "name": "South Holland" }
]
```

---

### 2. Get Compact Provinces

**GET** `/api/election/{year}/provinces/compact`

Returns provinces with just ID and name.

**Response**:
```json
[
  { "id": 1, "name": "North Holland" },
  { "id": 2, "name": "South Holland" }
]
```

---

### 3. Get Constituencies in a Province

**GET** `/api/election/{year}/provinces/{provinceId}/constituencies`

Returns all constituencies (regions) in one province.

**Path Parameters**:
- `provinceId`: The province ID

**Response**:
```json
[
  { "id": 101, "name": "Amsterdam" },
  { "id": 102, "name": "Haarlem" }
]
```

---

### 4. Get Compact Constituencies

**GET** `/api/election/{year}/provinces/{provinceId}/constituencies/compact`

Returns compact constituency list (ID + name only).

**Response**:
```json
[
  { "id": 101, "name": "Amsterdam" },
  { "id": 102, "name": "Haarlem" }
]
```

---

### 5. Get Votes Per Party

**GET** `/api/election/{year}/provinces/{provinceId}/parties`

Returns total votes per party in a province.

**Response**:
```json
{
  "1": { "id": 1, "name": "Green Party", "totalVotes": 52431 },
  "2": { "id": 2, "name": "Liberal Party", "totalVotes": 48723 }
}
```

---

## Overview

| Endpoint | Method | Description |
|---------|--------|-------------|
| `/` | GET | Get all provinces |
| `/compact` | GET | Get short version of provinces |
| `/{provinceId}/constituencies` | GET | Get constituencies of a province |
| `/{provinceId}/constituencies/compact` | GET | Get compact constituencies |
| `/{provinceId}/parties` | GET | Get party vote totals |

---

**Made for**: VoteU Election Project  
**Class**: `ProvinceController.java`  
**Framework**: Spring Boot  
