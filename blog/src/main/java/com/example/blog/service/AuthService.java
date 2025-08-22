package com.example.blog.service;

import com.example.blog.dto.*;
import com.example.blog.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthResponse register(AuthRequest req) {
        var user = userService.register(req.getEmail(), req.getPassword(), req.getName());
        String token = jwtUtils.generateToken(user.getEmail());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token); resp.setEmail(user.getEmail()); resp.setName(user.getName());
        return resp;
    }

    public AuthResponse login(AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        var user = userService.getByEmail(req.getEmail());
        String token = jwtUtils.generateToken(req.getEmail());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token); resp.setEmail(user.getEmail()); resp.setName(user.getName());
        return resp;
    }
}
