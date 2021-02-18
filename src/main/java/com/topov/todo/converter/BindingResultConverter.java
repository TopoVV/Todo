package com.topov.todo.converter;

import com.topov.todo.dto.InputError;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class BindingResultConverter {
    public List<InputError> convertBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
            .stream()
            .map(fieldError -> new InputError(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    }
}
