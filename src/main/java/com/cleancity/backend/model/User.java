package com.cleancity.backend.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private String id;
    private String email;
    private String passwordHash;
    private String status; // ACTIVE, INACTIVE, LOCKED

    // Nested structures
    private Map<String, Object> auth;
    private Map<String, Object> metadata;
    private Map<String, Object> profile;
    private Map<String, Object> attributes; // Flexible map

    public User() {
        this.status = "ACTIVE";
        this.auth = new HashMap<>();
        this.metadata = new HashMap<>();
        this.profile = new HashMap<>();
        this.attributes = new HashMap<>();
    }
}
