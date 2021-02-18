package com.topov.todo.service;

import com.topov.todo.model.User;

public interface JsonTokenService {
    String createAuthenticationToken(User user);
}
