package com.topov.todo.config.security;

import com.topov.todo.filter.AuthenticationFilter;
import com.topov.todo.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    private static final Logger log = LogManager.getLogger(SecurityConfig.class);

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(AuthenticationService authenticationService) {
        log.info("AuthnticationFilter registraion");
        final FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new AuthenticationFilter(authenticationService));
        filterRegistration.addUrlPatterns("/todos/*");

        return filterRegistration;
    }
}
