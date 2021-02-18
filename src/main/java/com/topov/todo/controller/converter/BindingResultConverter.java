package com.topov.todo.controller.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
public class BindingResultConverter {
    public Map<String, String> convertBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
            .stream()
            .collect(toMap(
                FieldError::getField,
                fieldError -> {
                    final String message = fieldError.getDefaultMessage();
                    final Optional<String> description = Optional.ofNullable(message);
                    return description.orElse("No description");
                }
            ));
    }
}
