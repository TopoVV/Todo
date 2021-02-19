package com.topov.todo.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.service.AuthenticationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class AuthenticationFilter implements Filter {
    private final ObjectMapper mapper = new ObjectMapper();

    private AuthenticationService authenticationService;


    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        final String body = req.getReader()
            .lines()
            .collect(joining(System.lineSeparator()));

        final AuthenticationParameters params = this.mapper.readValue(body, AuthenticationParameters.class);
        final Optional<String> tokenOptional = params.getToken();

        if (!tokenOptional.isPresent() || !this.authenticationService.isAuthenticated(tokenOptional.get())) {
            final HashMap<String, String> response = new HashMap<>();
            response.put("result", "fail");
            response.put("message", "Please login!");
            res.getWriter().write(this.mapper.writeValueAsString(response));
        }

        filterChain.doFilter(req, res);
    }

    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
    private static final class AuthenticationParameters {
        private String token;

        public Optional<String> getToken() {
            return Optional.ofNullable(token);
        }
    }
}
