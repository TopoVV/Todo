package com.topov.todo.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.service.AuthenticationService;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class AuthenticationFilter implements Filter {
    private static final Logger log = LogManager.getLogger(AuthenticationFilter.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private AuthenticationService authenticationService;


    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final String body = req.getReader()
            .lines()
            .collect(joining(System.lineSeparator()));

        if (body.isEmpty()) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write(buildUnauthorizedResponse());
            return;
        }

        final AuthenticationParameters params = this.mapper.readValue(body, AuthenticationParameters.class);
        final Optional<String> tokenOptional = params.getToken();

        if (!tokenOptional.isPresent() || !this.authenticationService.authenticateWithToken(tokenOptional.get())) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write(buildUnauthorizedResponse());
        }

        filterChain.doFilter(req, res);
    }

    private String buildUnauthorizedResponse() throws JsonProcessingException {
        final HashMap<String, Object> response = new HashMap<>();
        response.put("result", "fail");
        response.put("message", "Please login!");
        return this.mapper.writeValueAsString(response);
    }

    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static final class AuthenticationParameters {
        @JsonInclude(JsonInclude.Include.USE_DEFAULTS)
        private String token;

        public Optional<String> getToken() {
            return Optional.ofNullable(token);
        }
    }
}
