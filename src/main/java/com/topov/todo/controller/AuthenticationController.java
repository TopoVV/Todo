package com.topov.todo.controller;

import com.topov.todo.converter.BindingResultConverter;
import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.service.AuthenticationServiceImpl;
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

@RestController
public class AuthenticationController {

    private BindingResultConverter bindingResultConverter;
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Autowired
    public AuthenticationController(BindingResultConverter bindingResultConverter) {
        this.bindingResultConverter = bindingResultConverter;
    }

    @PostMapping(
        value = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> authenticationPost(@RequestBody @Valid AuthenticationData authenticationData, BindingResult bindingResult) {
        final HashMap<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            response.put("result", "fail");
            response.put("message", "Invalid input");
            response.put("errors", this.bindingResultConverter.convertBindingResult(bindingResult));
            return response;

        }

        return Collections.singletonMap("result", "success");
    }
}
