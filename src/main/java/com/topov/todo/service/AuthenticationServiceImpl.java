package com.topov.todo.service;

import com.sun.security.auth.UserPrincipal;
import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.dto.Authentication;
import com.topov.todo.model.User;
import com.topov.todo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JsonTokenService jsonTokenService;
    
    private static ThreadLocal<Principal> currentUser = ThreadLocal.withInitial(() -> null);

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JsonTokenService jsonTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jsonTokenService = jsonTokenService;
    }

    @Override
    public Authentication authenticateUser(AuthenticationData authenticationData) {
        final Optional<User> userOptional = this.userRepository.findByUsername(authenticationData.getUsername());
        final String encodedPassword = this.passwordEncoder.encodePassword(authenticationData.getPassword());

        if (!userOptional.isPresent() || !userOptional.get().getPassword().equals(encodedPassword)) {
            return new Authentication("Invalid username or password");
        }

        final User user = userOptional.get();
        final String tokenValue = this.jsonTokenService.createAuthenticationToken(user);
        return new Authentication(tokenValue, "Welcome, " + user.getUsername());
    }

    @Override
    public boolean authenticateWithToken(String token) {
        try {
            final Claims claims = this.jsonTokenService.parseClaims(token);
            final Optional<User> userOptional = this.userRepository.findByUsername(claims.getSubject());

            if (userOptional.isPresent()) {
                currentUser.set(new UserPrincipal(claims.getSubject()));
                return true;
            }

            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public Principal getCurrentUser() {
        return Optional.ofNullable(currentUser.get())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }


}
