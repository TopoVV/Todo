package com.topov.todo.service;

import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.dto.Authentication;
import com.topov.todo.model.User;
import com.topov.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JsonTokenService jsonTokenService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JsonTokenService jsonTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jsonTokenService = jsonTokenService;
    }

    public Authentication authenticateUser(AuthenticationData authenticationData) {
        final Optional<User> userOptional = this.userRepository.findByUsername(authenticationData.getUsername());
        final String encodedPassword = this.passwordEncoder.encodePassword(authenticationData.getPassword());

        if (!userOptional.isPresent() || !userOptional.get().getPassword().equals(encodedPassword)) {
            return new Authentication("Invalid username or password");
        }

        final User user = userOptional.get();
        final String tokenValue = this.jsonTokenService.createAuthenticationToken(user);
        return new Authentication("Welcome, " + user.getUsername(), tokenValue);
    }

}
