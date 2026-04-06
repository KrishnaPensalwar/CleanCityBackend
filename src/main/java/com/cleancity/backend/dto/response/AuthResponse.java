package com.cleancity.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String email;

    public AuthResponse(String token, String id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }
}
