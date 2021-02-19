package com.topov.todo.service;

import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.AuthenticationData;

public interface AuthenticationService {
    Authentication authenticateUser(AuthenticationData authenticationData);
    boolean isAuthenticated(String token);
}
