package com.topov.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class Authentication {
    private boolean isSuccessful;
    private String message;
    private String tokenValue;

    public Authentication(String tokenValue, String message) {
        this.tokenValue = tokenValue;
        this.message = message;
        this.isSuccessful = true;
    }

    public Authentication(String message) {
        this.isSuccessful = false;
        this.message = message;
    }

    public Optional<String> getTokenValue() {
        return Optional.ofNullable(tokenValue);
    }
}
