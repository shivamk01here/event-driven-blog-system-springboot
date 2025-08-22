package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public User register(String email, String password, String name) {
        if (userRepo.findByEmail(email).isPresent()) throw new RuntimeException("Email already taken");
        User user = User.builder().email(email).password(encoder.encode(password)).name(name).build();
        return userRepo.save(user);
    }

    public User getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow();
    }
}
