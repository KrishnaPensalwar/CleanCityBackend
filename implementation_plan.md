# CleanCity Backend Implementation Plan

This plan outlines the specific steps and files we will create to implement the Scalable Spring Boot + Firebase backend architecture we designed. 

## User Review Required

> [!IMPORTANT]
> Because it involves creating an entire codebase, please review this plan and approve it. Once you approve, I will automatically execute all these steps, generating the project files in the current workspace. 

## Proposed Changes

We will construct a standard Maven-based Spring Boot project from scratch in the current directory (`/Users/krishnapensalwar/Documents/CleanCity Backend`).

### Project Setup & Dependencies

#### [NEW] [pom.xml](file:///Users/krishnapensalwar/Documents/CleanCity Backend/pom.xml)
Will contain dependencies for Spring Boot 3.x, Spring Web, Spring Security, Validation, Firebase Admin SDK, JJWT (for tokens), and Lombok.

#### [NEW] [application.yml](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/resources/application.yml)
Will contain the configuration application properties, such as Firebase project setup, JWT secrets, and logging levels.

---

### Application Core

#### [NEW] [CleanCityApplication.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/CleanCityApplication.java)
The main Spring Boot bootstrap application class.

---

### Configurations

#### [NEW] [FirebaseConfig.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/config/FirebaseConfig.java)
Initializes the `FirebaseApp` using the Firebase Admin SDK.

#### [NEW] [SecurityConfig.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/config/SecurityConfig.java)
Configures Spring Security to ignore standard stateful sessions, configures CORS, allows unauthenticated access to login/signup endpoints, and registers the JWT verification filter.

#### [NEW] [JwtConfig.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/config/JwtConfig.java)
Utility component for generating, verifying, and extracting claims from JWTs using `jjwt`.

---

### Data Models & DTOs

#### [NEW] [User.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/model/User.java)
The domain model representing the user structure in Firestore.

#### [NEW] [SignupRequest.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/dto/request/SignupRequest.java)
#### [NEW] [LoginRequest.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/dto/request/LoginRequest.java)
#### [NEW] [AuthResponse.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/dto/response/AuthResponse.java)
Data Transfer Objects with `@Valid` annotations for request validation.

---

### Repository Layer (Firebase Integration)

#### [NEW] [UserRepository.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/repository/UserRepository.java)
Interface for user data operations.

#### [NEW] [UserRepositoryImpl.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/repository/UserRepositoryImpl.java)
Implementation utilizing `Firestore` to find users by email, and to save/update user documents.

---

### Service Layer (Business Logic)

#### [NEW] [AuthService.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/service/AuthService.java)
Handles the core business logic: checking for duplicate users, hashing passwords with BCrypt, interacting with `JwtConfig` to generate tokens, and managing the OTP logic for resetting passwords.

---

### Security Filters

#### [NEW] [JwtAuthenticationFilter.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/filter/JwtAuthenticationFilter.java)
Intercepts all incoming HTTP requests to validate the `Authorization: Bearer <token>` header before hitting the controllers.

---

### Controllers

#### [NEW] [AuthController.java](file:///Users/krishnapensalwar/Documents/CleanCity Backend/src/main/java/com/cleancity/backend/controller/AuthController.java)
Exposes the `/api/v1/auth/signup`, `/api/v1/auth/login`, and password reset REST endpoints.

## Open Questions

1. **Firebase Service Account:** You will need a `firebase-service-account.json`. When setting up, I will write the code to look for it, but you'll have to manually drop your actual JSON key from the Firebase Console into the `src/main/resources` folder since I cannot download that for you. Is that acceptable?
2. **Java Version:** I am defaulting to **Java 17**, which is standard for modern Spring Boot 3.x. Please let me know if you are targeting Java 21 instead.

## Verification Plan

### Automated Verification
- Once scaffolding is complete, I will run `mvn clean compile` to ensure all generated code compiles perfectly and there are no syntax or Spring component wiring errors.
