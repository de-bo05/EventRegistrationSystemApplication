# API Documentation

Base URL: `http://localhost:8080/api`

All authenticated endpoints require JWT token in header:
```
Authorization: Bearer <your_jwt_token>
```

## Authentication Endpoints

### Register User
**POST** `/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "role": "USER"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": 1,
  "username": "john_doe"
}
```

### Login
**POST** `/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "role": "USER",
  "userId": 1,
  "fullName": "John Doe"
}
```

## Event Endpoints

### Get Active Events (Public)
**GET** `/events/public/active`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Tech Conference 2024",
    "description": "Annual technology conference",
    "eventDate": "2024-12-15T10:00:00",
    "venue": "Convention Center",
    "capacity": 100,
    "registeredCount": 45,
    "isActive": true,
    "createdAt": "2024-01-01T00:00:00",
    "createdById": 1,
    "createdByUsername": "admin"
  }
]
```

### Get Upcoming Events (Public)
**GET** `/events/public/upcoming`

**Response:** Same as above

### Get Event by ID (Public)
**GET** `/events/public/{id}`

**Response:** Single event object

### Create Event (Authenticated)
**POST** `/events`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "New Event",
  "description": "Event description",
  "eventDate": "2024-12-20T14:00:00",
  "venue": "Event Venue",
  "capacity": 50
}
```

**Response:** Event object

### Update Event (Authenticated)
**PUT** `/events/{id}`

**Request Body:** Same as create (all fields optional)

### Delete Event (Authenticated)
**DELETE** `/events/{id}`

**Response:**
```json
{
  "message": "Event deleted successfully"
}
```

### Get My Events (Authenticated)
**GET** `/events/my-events`

**Response:** Array of events created by current user

## Registration Endpoints

### Register for Event (Authenticated)
**POST** `/registrations/event/{eventId}`

**Response:**
```json
{
  "message": "Successfully registered for event",
  "registrationId": 1,
  "ticketNumber": "TKT-1234567890-1234",
  "qrCode": "data:image/png;base64,iVBORw0KGgo..."
}
```

### Get My Registrations (Authenticated)
**GET** `/registrations/my-registrations`

**Response:**
```json
[
  {
    "id": 1,
    "user": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe"
    },
    "event": {
      "id": 1,
      "name": "Tech Conference 2024",
      "eventDate": "2024-12-15T10:00:00",
      "venue": "Convention Center"
    },
    "ticketNumber": "TKT-1234567890-1234",
    "qrCodeData": "data:image/png;base64,...",
    "confirmed": true,
    "registeredAt": "2024-01-10T10:00:00"
  }
]
```

### Get Registration by Ticket Number (Authenticated)
**GET** `/registrations/ticket/{ticketNumber}`

**Response:** Registration object

### Cancel Registration (Authenticated)
**DELETE** `/registrations/{id}`

**Response:**
```json
{
  "message": "Registration cancelled successfully"
}
```

## Attendance Endpoints

### Mark Attendance by Ticket Number (Authenticated)
**POST** `/attendance/mark/ticket/{ticketNumber}`

**Response:**
```json
{
  "message": "Attendance marked successfully",
  "attendanceId": 1
}
```

### Mark Attendance by Registration ID (Authenticated)
**POST** `/attendance/mark/registration/{registrationId}`

**Response:** Same as above

### Get Event Attendance (Authenticated)
**GET** `/attendance/event/{eventId}`

**Response:**
```json
[
  {
    "id": 1,
    "registration": {
      "id": 1,
      "ticketNumber": "TKT-1234567890-1234",
      "user": {
        "id": 1,
        "fullName": "John Doe"
      },
      "event": {
        "id": 1,
        "name": "Tech Conference 2024"
      }
    },
    "attended": true,
    "markedAt": "2024-12-15T10:30:00",
    "markedBy": {
      "id": 2,
      "username": "admin"
    }
  }
]
```

### Get All Attendances (Authenticated)
**GET** `/attendance/all`

**Response:** Array of all attendance records

## Admin Endpoints

### Get All Users (Admin Only)
**GET** `/admin/users`

**Response:**
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "enabled": true,
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

### Get User by ID (Admin Only)
**GET** `/admin/users/{id}`

**Response:** User object

### Delete User (Admin Only)
**DELETE** `/admin/users/{id}`

**Response:**
```json
{
  "message": "User deleted successfully"
}
```

## Error Responses

All endpoints may return error responses:

```json
{
  "error": "Error message here"
}
```

**Common HTTP Status Codes:**
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Validation Rules

### User Registration
- `username`: 3-50 characters, unique
- `email`: Valid email format, unique
- `password`: Minimum 6 characters
- `fullName`: Required

### Event Creation
- `name`: Required
- `eventDate`: Required, must be in future
- `venue`: Required
- `capacity`: Required, minimum 1

## Example cURL Commands

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123","fullName":"Test User"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Create Event (with token)
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name":"Test Event","description":"Test","eventDate":"2024-12-20T14:00:00","venue":"Test Venue","capacity":50}'
```

### Register for Event
```bash
curl -X POST http://localhost:8080/api/registrations/event/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Mark Attendance
```bash
curl -X POST http://localhost:8080/api/attendance/mark/ticket/TKT-1234567890-1234 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

