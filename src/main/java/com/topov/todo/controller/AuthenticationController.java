package com.topov.todo.controller;

import com.topov.todo.dto.AuthenticationData;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
public class AuthenticationController {

    @PostMapping(
        value = "/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> authenticationPost(@RequestBody @Valid AuthenticationData authenticationData, BindingResult bindingResult) {

        return Collections.singletonMap("result", "success");
    }
}
