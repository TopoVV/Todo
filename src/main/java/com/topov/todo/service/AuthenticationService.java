package com.topov.todo.service;

import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.request.AuthenticationData;

import java.security.Principal;

public interface AuthenticationService {
    Authentication authenticateUser(AuthenticationData authenticationData);
    boolean authenticateWithToken(String token);
    Principal getCurrentUser();
}
