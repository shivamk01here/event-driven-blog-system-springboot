package com.example.blog.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private String name;
}
