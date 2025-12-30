package com.example.api.service;

import com.example.api.dao.UserDao;
import com.example.api.model.User;
import com.example.api.util.JwtUtil;

import java.util.Map;
import java.util.Optional;

public class AuthService {

    private final UserDao dao = new UserDao();
    public Map<String,String> login(String username, String password) {
        Optional<User> userOpt = dao.findByUsername(username);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        String accessToken = JwtUtil.generateAccessToken(username);
        String refreshToken = JwtUtil.generateRefreshToken(username);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public Map<String,String> refresh(String refreshToken) {
        Map<String,String> tokens = JwtUtil.rotateRefreshToken(refreshToken);
        if(tokens == null) throw new RuntimeException("Invalid refresh token");
        return tokens;
    }
}

