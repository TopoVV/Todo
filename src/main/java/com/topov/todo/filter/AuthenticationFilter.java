package com.topov.todo.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static java.util.stream.Collectors.joining;

public class AuthenticationFilter implements Filter {
    private final ObjectMapper mapper = new ObjectMapper();
    private AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final String token = ((HttpServletRequest) req).getHeader(HttpHeaders.AUTHORIZATION);

        if (token == null || token.isEmpty()) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write(unauthorizedResponse());
            return;
        }

        if (!this.authenticationService.authenticateWithToken(token)) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write(unauthorizedResponse());
        }

        filterChain.doFilter(req, res);
    }

    private String unauthorizedResponse() throws JsonProcessingException {
        final HashMap<String, Object> response = new HashMap<>();
        response.put("result", "fail");
        response.put("message", "Please login!");
        return this.mapper.writeValueAsString(response);
    }
}
