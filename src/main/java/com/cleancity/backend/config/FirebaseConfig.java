package com.cleancity.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                GoogleCredentials credentials;
                
                String firebaseConfigJson = System.getenv("FIREBASE_CONFIG");
                if (firebaseConfigJson != null && !firebaseConfigJson.trim().isEmpty()) {
                    // Option A: Initialize from raw JSON string (Great for CI/CD or PaaS like Heroku/Render)
                    InputStream serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
                    credentials = GoogleCredentials.fromStream(serviceAccount);
                    logger.info("Initializing Firebase using FIREBASE_CONFIG JSON string.");
                } else {
                    // Option B: Initialize using GOOGLE_APPLICATION_CREDENTIALS (Great for local dev or GCP/AWS)
                    // This natively looks for the GOOGLE_APPLICATION_CREDENTIALS env var.
                    credentials = GoogleCredentials.getApplicationDefault();
                    logger.info("Initializing Firebase using GOOGLE_APPLICATION_CREDENTIALS.");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized successfully");
            }
        } catch (Exception e) {
            logger.error("Error initializing Firebase: {}", e.getMessage());
            throw new RuntimeException("FATAL: Could not initialize Firebase! Ensure environment variables are set.", e);
        }
    }
}
