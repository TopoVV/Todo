package com.topov.todo.service;

import com.topov.todo.dto.Authentication;
import com.topov.todo.dto.request.AuthenticationData;
import com.topov.todo.model.User;
import com.topov.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AuthenticationServiceImplTest.AuthenticationServiceTestContext.class })
class AuthenticationServiceImplTest {

    private JsonTokenService tokenService;

    @Autowired
    AuthenticationServiceImplTest(JsonTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Test
    public void whenPasswordWrong_ThenIsNotSuccessful() {
        final User mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn("Password");

        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        final PasswordEncoder mockEncoder = mock(PasswordEncoderImpl.class);
        when(mockEncoder.encodePassword(anyString())).thenReturn("Wrong password");

        final JsonTokenService mockTokenService = mock(JsonTokenService.class);

        final AuthenticationService service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Wrong password"));
        assertFalse(authentication.getTokenValue().isPresent());
    }

    @Test
    public void whenUsernameWrong_ThenNotSuccessful() {
        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        final PasswordEncoder mockEncoder = mock(PasswordEncoderImpl.class);
        when(mockEncoder.encodePassword(anyString())).thenReturn("Password");

        final JsonTokenService mockTokenService = mock(JsonTokenService.class);

        final AuthenticationService service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Password"));
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

        final AuthenticationService service = new AuthenticationServiceImpl(mockUserRepository, mockEncoder, mockTokenService);
        final Authentication authentication = service.authenticateUser(new AuthenticationData("username", "Password"));
        assertTrue(authentication.getTokenValue().isPresent());
    }

    @Test
    public void authenticationTokenTest_MustBeUniqueUserForEachThread() throws ExecutionException, InterruptedException {
        final User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("name1");
        when(user1.getPassword()).thenReturn("password1");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername("name1")).thenReturn(Optional.of(user1));
        final PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);
        when(mockPasswordEncoder.encodePassword("password1")).thenReturn("password1");


        final AuthenticationServiceImpl authService = new AuthenticationServiceImpl(mockUserRepository, mockPasswordEncoder, this.tokenService);
        final String token1 = authService.authenticateUser(new AuthenticationData("name1", "password1"))
            .getTokenValue()
            .get();


        final User user2 = mock(User.class);
        when(user2.getUsername()).thenReturn("name2");
        when(user2.getPassword()).thenReturn("password2");
        when(mockUserRepository.findByUsername("name2")).thenReturn(Optional.of(user2));
        when(mockPasswordEncoder.encodePassword("password2")).thenReturn("password2");
        final String token2 = authService.authenticateUser(new AuthenticationData("name2", "password2"))
            .getTokenValue()
            .get();

        final Callable<Principal> thread1 = () ->  {
            authService.authenticateWithToken(token1);
            return authService.getCurrentUser();
        };
        final Callable<Principal> thread2 = () -> {
            authService.authenticateWithToken(token2);
            return authService.getCurrentUser();
        };

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final Future<Principal> submit1 = executorService.submit(thread1);
        final Future<Principal> submit2 = executorService.submit(thread2);

        submit1.get();
        submit2.get();
    }

    @TestConfiguration
    public static class AuthenticationServiceTestContext {
        @Bean
        public JsonTokenService jsonTokenService() {
            return new JsonTokenServiceImpl();
        }
    }

}
