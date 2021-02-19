package com.topov.todo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.filter.AuthenticationFilter;
import com.topov.todo.repository.UserRepository;
import com.topov.todo.service.AuthenticationService;
import com.topov.todo.service.AuthenticationServiceImpl;
import com.topov.todo.service.JsonTokenService;
import com.topov.todo.service.JsonTokenServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SecurityTest.SecurityTestContext.class})
public class SecurityTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private JsonTokenService jsonTokenService;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public SecurityTest(JsonTokenService jsonTokenService) {
        this.jsonTokenService = jsonTokenService;
    }

    @Test
    public void whenNoTokenProvided_ThenRejected() throws Exception {
        final AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);
        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new SecurityTestController())
            .addFilter(new AuthenticationFilter(mockAuthenticationService), "/secured")
            .build();

        final HashMap<String, String> body = new HashMap<>();


        mvc.perform(get("/secured")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }

    @Test
    public void whenTokenOk_ThenOk() throws Exception {
        final AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);
        Mockito.when(mockAuthenticationService.isAuthenticated(ArgumentMatchers.anyString())).thenReturn(true);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new SecurityTestController())
            .addFilter(new AuthenticationFilter(mockAuthenticationService), "/secured")
            .build();

        final HashMap<String, String> body = new HashMap<>();
        body.put("token", "my-token");


        mvc.perform(get("/secured")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("secured")));
    }


    @Test
    public void dowork() throws Exception {
        Calendar cal = Calendar.getInstance();

        final String token = Jwts.builder()
            .setIssuedAt(new Date())
            .setSubject("myname")
            .setExpiration(new Date(cal.getTimeInMillis() - (30L * 60000)))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();

        AuthenticationServiceImpl authService = new AuthenticationServiceImpl(null, null, this.jsonTokenService);
        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new SecurityTestController())
            .addFilter(new AuthenticationFilter(authService), "/secured")
            .build();

        final HashMap<String, String> body = new HashMap<>();
        body.put("token", token);


        mvc.perform(get("/secured")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }

    @TestConfiguration
    public static class SecurityTestContext {
        @Bean
        public JsonTokenService jsonTokenService() {
            return new JsonTokenServiceImpl();
        }
    }

    @RestController
    public static class SecurityTestController {

        @GetMapping("/secured")
        public Map<String, String> secured() {
            return Collections.singletonMap("result", "secured");
        }
    }
}
