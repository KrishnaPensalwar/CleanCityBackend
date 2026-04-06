# CleanCity Backend - API Testing Guide

This guide contains all the endpoints generated in the application, along with sample Request and Response formats so you can easily test them via Postman, Insomnia, or cURL.

**Base URL:** `http://localhost:8080/api/v1/auth`

---

## 1. Signup

Registers a new user into Firebase and returns a JWT token.

*   **Endpoint:** `POST /signup`
*   **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "email": "testuser@example.com",
  "password": "mySecurePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ...",
  "type": "Bearer",
  "id": "e421cd77-981b-4b13-88c9-9523f4aa123b",
  "email": "testuser@example.com"
}
```

---

## 2. Login

Authenticates an existing user and returns a fresh JWT token.

*   **Endpoint:** `POST /login`
*   **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "email": "testuser@example.com",
  "password": "mySecurePassword123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ...",
  "type": "Bearer",
  "id": "e421cd77-981b-4b13-88c9-9523f4aa123b",
  "email": "testuser@example.com"
}
```

---

## 3. Forgot Password

Initiates the password reset flow. Under the hood, this generates an OTP and attaches it to the user's Firestore document.

*   **Endpoint:** `POST /forgot-password`
*   **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "email": "testuser@example.com"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "If an account with that email exists, an OTP will be sent."
}
```

*(Note: To test the next step, you will need to open your Firebase Console, look at the `users` collection, find your user, and copy the 6-digit OTP stored in the `auth.resetToken` field, since we haven't hooked up an Email sending provider like SendGrid yet).*

---

## 4. Reset Password

Finalizes the password reset using the OTP generated in step 3.

*   **Endpoint:** `POST /reset-password`
*   **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "email": "testuser@example.com",
  "otp": "123456", 
  "newPassword": "NewStrongPassword456"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Password has been successfully reset."
}
```
