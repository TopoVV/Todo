package com.topov.todo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.dto.RegistrationData;
import com.topov.todo.service.RegistrationService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registrationPost_Ok() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);
        Mockito.when(registrationService.registerUser(any())).thenReturn(true);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService))
            .build();

        final RegistrationData object = new RegistrationData("username", "password");
        final String json = this.mapper.writeValueAsString(object);
        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("success")));
    }

    @Test
    public void registrationPost_UsernameTaken() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);
        Mockito.when(registrationService.registerUser(any())).thenReturn(false);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService))
            .build();

        final RegistrationData object = new RegistrationData("username", "password");
        final String json = this.mapper.writeValueAsString(object);
        mvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }

    @Test
    public void registrationPost_Error() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);
        Mockito.when(registrationService.registerUser(any())).thenThrow(RuntimeException.class);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService))
            .build();

        final RegistrationData object = new RegistrationData("username", "password");
        final String json = this.mapper.writeValueAsString(object);
        mvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }
}
