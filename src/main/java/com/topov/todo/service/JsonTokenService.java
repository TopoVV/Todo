package com.topov.todo.service;

import com.topov.todo.model.User;
import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JsonTokenService {
    String createAuthenticationToken(User user);
    void verifyToken(String token);
}
