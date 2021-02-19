package com.topov.todo.exception;

import lombok.Getter;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String s) {
        super(s);
    }
}
