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
    private String message;
    private String tokenValue;

    public Authentication(String tokenValue, String message) {
        this.tokenValue = tokenValue;
        this.message = message;
    }

    public Authentication(String message) {
        this.message = message;
    }

    public Optional<String> getTokenValue() {
        return Optional.ofNullable(tokenValue);
    }
}
