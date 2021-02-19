package com.topov.todo.service;

import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.model.User;

import java.security.Principal;
import java.util.Optional;

public interface AuthenticationService {
    Authentication authenticateUser(AuthenticationData authenticationData);
    boolean authenticateWithToken(String token);
    Optional<Principal> getCurrentUser();
}
