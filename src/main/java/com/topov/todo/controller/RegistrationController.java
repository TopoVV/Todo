package com.topov.todo.controller;

import com.topov.todo.controller.converter.BindingResultConverter;
import com.topov.todo.dto.RegistrationData;
import com.topov.todo.service.RegistrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@RestController
public class RegistrationController {
    private static final Logger log = LogManager.getLogger(RegistrationController.class);

    private RegistrationService registrationService;
    private BindingResultConverter bindingResultConverter;

    @Autowired
    public RegistrationController(RegistrationService registrationService, BindingResultConverter bindingResultConverter) {
        this.registrationService = registrationService;
        this.bindingResultConverter = bindingResultConverter;
    }

    @PostMapping(
        value = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> registrationPost(@RequestBody @Valid RegistrationData registrationData, BindingResult bindingResult) {
        final HashMap<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("result", "fail");
            response.put("message", "Invalid input");
            response.putAll(this.bindingResultConverter.convertBindingResult(bindingResult));
            return response;
        }

        try {
            if (this.registrationService.registerUser(registrationData)) {
                response.put("result", "success");
                response.put("message", "OK. Welcome");
                return response;
            }

            response.put("result", "fail");
            response.put("message", "Fail. Username is already in use");
            return response;
        } catch (RuntimeException e) {
            log.error("Error occurred during registration", e);

            response.put("result", "fail");
            response.put("message", "Unknown error. Try again later");
            return response;
        }
    }
}
