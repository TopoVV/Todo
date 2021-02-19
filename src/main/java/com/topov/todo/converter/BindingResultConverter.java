package com.topov.todo.converter;

import com.topov.todo.dto.response.InputError;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
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
