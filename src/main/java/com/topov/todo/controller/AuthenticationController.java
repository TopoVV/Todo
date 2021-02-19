package com.topov.todo.controller;

import com.topov.todo.converter.BindingResultConverter;
import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(
        value = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> authenticationPost(@RequestBody @Valid AuthenticationData authenticationData) {
        final HashMap<String, Object> response = new HashMap<>();

        final Authentication authentication = authenticationService.authenticateUser(authenticationData);
        response.put("message", authentication.getMessage());

        final Optional<String> tokenOptional = authentication.getTokenValue();
        if (tokenOptional.isPresent()) {
            response.put("result", "success");
            response.put("token", tokenOptional.get());
        }

        return response;
    }
}
