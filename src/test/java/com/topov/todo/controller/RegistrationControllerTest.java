package com.topov.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topov.todo.converter.BindingResultConverter;
import com.topov.todo.dto.RegistrationData;
import com.topov.todo.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RegistrationControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registrationPost_Ok() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);
        when(registrationService.registerUser(any())).thenReturn(true);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService, new BindingResultConverter()))
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
        when(registrationService.registerUser(any())).thenReturn(false);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService, new BindingResultConverter()))
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
        when(registrationService.registerUser(any())).thenThrow(RuntimeException.class);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService, new BindingResultConverter()))
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
    public void registrationPost_EmptyUsername() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);
        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService, new BindingResultConverter()))
            .build();

        final RegistrationData object = new RegistrationData("", "password");
        final String json = this.mapper.writeValueAsString(object);
        mvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }

    @Test
    public void registrationPost_MultipleInputErrors() throws Exception {
        final RegistrationService registrationService = mock(RegistrationService.class);

        MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new RegistrationController(registrationService, new BindingResultConverter()))
            .build();

        final RegistrationData object = new RegistrationData("", "");
        final String json = this.mapper.writeValueAsString(object);
        mvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(print())
            .andExpect(jsonPath("$.result", is("fail")));
    }
}
