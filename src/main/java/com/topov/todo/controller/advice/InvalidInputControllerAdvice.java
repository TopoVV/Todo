package com.topov.todo.controller.advice;

import com.topov.todo.converter.BindingResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@RestControllerAdvice
public class InvalidInputControllerAdvice extends ResponseEntityExceptionHandler {

    private BindingResultConverter bindingResultConverter;

    @Autowired
    public InvalidInputControllerAdvice(BindingResultConverter bindingResultConverter) {
        this.bindingResultConverter = bindingResultConverter;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final BindingResult bindingResult = ex.getBindingResult();

        final HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", "fail");
        responseBody.put("message", "Invalid input");
        responseBody.put("errors", this.bindingResultConverter.convertBindingResult(bindingResult));

        return ResponseEntity.badRequest().body(responseBody);
    }
}
