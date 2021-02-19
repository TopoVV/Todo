package com.topov.todo.config;

import com.topov.todo.filter.AuthenticationFilter;
import com.topov.todo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {


    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(AuthenticationService authenticationService) {
        final FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new AuthenticationFilter(authenticationService));
        filterRegistration.addUrlPatterns("/todos");

        return filterRegistration;
    }
}
