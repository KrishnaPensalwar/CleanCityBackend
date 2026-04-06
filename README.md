# CleanCity Backend

A scalable Spring Boot backend using Firebase Firestore for data storage and a custom JWT-based authentication system.

## Setup & Configuration

This backend requires Firebase to run. **No `.json` credentials files are stored in the repository for security reasons.** You must configure your environment variables.

### Local Development (Option 1: Using File Path)

The easiest way to run the application locally is by utilizing the built-in Google Auth library mechanism using `GOOGLE_APPLICATION_CREDENTIALS`.

1. Generate your service account JSON file from Firebase.
2. Save it somewhere secure on your computer (e.g. `~/.secrets/firebase-service-account.json`). **Do not put it inside the project folder.**
3. Export the environment variable pointing to that file before running the Spring Boot application:

**Mac/Linux Terminal:**
```bash
export GOOGLE_APPLICATION_CREDENTIALS="/Users/yourname/.secrets/firebase-service-account.json"
./mvnw spring-boot:run
```

**If using IntelliJ IDEA / Eclipse:**
1. Open your "Run Configuration" for `CleanCityApplication`.
2. Find the "Environment Variables" field.
3. Add: `GOOGLE_APPLICATION_CREDENTIALS=/Users/yourname/.secrets/firebase-service-account.json`

### Local Development (Option 2: Using `.env` via plugin or docker)

If your setup supports loading a `.env` file automatically (like when running via Docker, or if you install `dotenv-java`), you can define it like this:

```env
# .env file content
GOOGLE_APPLICATION_CREDENTIALS=/absolute/path/to/your/firebase-service-account.json
```

---

### Production Deployment (PaaS, Docker, Render, Heroku)

For production, or deploying where you don't have a reliable filesystem, you can pass the *entire JSON string* natively using the `FIREBASE_CONFIG` variable:

1. Copy the entire contents of your `firebase-service-account.json` file.
2. Set it as an environment variable in your production host (e.g., Heroku Config Vars, Render Environment, Docker ENV):

```bash
FIREBASE_CONFIG='{
  "type": "service_account",
  "project_id": "cleancity-xxxxx",
  "private_key_id": "xxx...",
  "private_key": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n",
  "client_email": "firebase-adminsdk-xxx.iam.gserviceaccount.com"
}'
```

When `FIREBASE_CONFIG` is detected, the application will automatically read the credentials from memory instead of the filesystem.
