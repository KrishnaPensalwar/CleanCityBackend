package com.cleancity.backend.repository;

import com.cleancity.backend.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String COLLECTION_NAME = "users";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            CollectionReference users = getFirestore().collection(COLLECTION_NAME);
            ApiFuture<QuerySnapshot> query = users.whereEqualTo("email", email).get();
            QuerySnapshot querySnapshot = query.get();

            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                User user = document.toObject(User.class);
                if (user != null) {
                    user.setId(document.getId());
                }
                return Optional.ofNullable(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        try {
            if (user.getId() == null || user.getId().isEmpty()) {
                user.setId(UUID.randomUUID().toString());
            }

            DocumentReference docRef = getFirestore().collection(COLLECTION_NAME).document(user.getId());
            ApiFuture<com.google.cloud.firestore.WriteResult> result = docRef.set(user);
            result.get(); // Wait to confirm it wrote securely
            
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    @Override
    public void update(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            throw new IllegalArgumentException("User ID must be present for an update");
        }
        try {
            DocumentReference docRef = getFirestore().collection(COLLECTION_NAME).document(user.getId());
            docRef.set(user).get(); // Overwrites document with updated map
        } catch (Exception e) {
            throw new RuntimeException("Error updating user", e);
        }
    }
}
