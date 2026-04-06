package com.cleancity.backend.repository;

import com.cleancity.backend.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
    void update(User user);
}
