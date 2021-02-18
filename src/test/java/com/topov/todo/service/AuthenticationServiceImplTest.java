package com.topov.todo.service;

import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.AuthenticationData;
import com.topov.todo.model.User;
import com.topov.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTest {

    @Test
    public void whenPasswordWrong_ThenIsNotSuccessful() {
        final User mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn("Password");

        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        final PasswordEncoder mockEncoder = mock(PasswordEncoderImpl.class);
        when(mockEncoder.encodePassword(anyString())).thenReturn("Wrong password");

        final JsonTokenService mockTokenService = mock(JsonTokenService.class);

        final AuthenticationServiceImpl service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Wrong password"));
        assertFalse(authentication.isSuccessful());
        assertFalse(authentication.getTokenValue().isPresent());
    }

    @Test
    public void whenUsernameWrong_ThenNotSuccessful() {
        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final PasswordEncoder mockEncoder = mock(PasswordEncoderImpl.class);
        when(mockEncoder.encodePassword(anyString())).thenReturn("Password");

        final JsonTokenService mockTokenService = mock(JsonTokenService.class);

        final AuthenticationServiceImpl service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Password"));
        assertFalse(authentication.isSuccessful());
        assertFalse(authentication.getTokenValue().isPresent());
    }

    @Test
    public void whenUsernameAndPasswordOK_ThenSuccessful() {
        final User mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn("Password");

        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        final PasswordEncoder mockEncoder = mock(PasswordEncoderImpl.class);
        when(mockEncoder.encodePassword(anyString())).thenReturn("Password");

        final JsonTokenService mockTokenService = mock(JsonTokenService.class);
        when(mockTokenService.createAuthenticationToken(mockUser)).thenReturn("token");

        final AuthenticationServiceImpl service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Password"));
        assertTrue(authentication.isSuccessful());
        assertTrue(authentication.getTokenValue().isPresent());
    }

}
