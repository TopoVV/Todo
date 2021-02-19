package com.topov.todo.service;

import com.sun.security.auth.UserPrincipal;
import com.topov.todo.model.Todo;
import com.topov.todo.dto.request.TodoData;
import com.topov.todo.model.User;
import com.topov.todo.repository.TodoRepository;
import com.topov.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodoServiceImplTest {

    @Test
    public void whenCreateTodoAuthenticated_ThenReturnTrue() {
        final Principal principal = new UserPrincipal("username");
        final AuthenticationService mockAuthService = mock(AuthenticationService.class);
        when(mockAuthService.getCurrentUser()).thenReturn(principal);
        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(principal.getName())).thenReturn(Optional.of(mock(User.class)));
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthService);
        assertDoesNotThrow(() -> todoService.createTodo(new TodoData("text todo")));
  }


    @Test
    public void whenCreateTodoNotAuthenticated_ThenThrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("username");

        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername(principal.getName())).thenReturn(Optional.of(mock(User.class)));

        final TodoRepository mockTodoRepository = mock(TodoRepository.class);

        final AuthenticationServiceImpl authService =
            new AuthenticationServiceImpl(mockUserRepository, mock(PasswordEncoder.class), mock(JsonTokenService.class));

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, authService);
        assertThrows(ResponseStatusException.class, () -> todoService.createTodo(new TodoData("text todo")));
    }

    @Test
    public void whenCreateTodoUnknownAuthentication_ThenThrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("uname");

        final UserRepository mockUserRepository = mock(UserRepository.class);
        when(mockUserRepository.findByUsername("username")).thenReturn(Optional.of(mock(User.class)));

        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertThrows(ResponseStatusException.class, () -> todoService.createTodo(new TodoData("text todo")));
    }

    @Test
    public void whenUpdateNotOwnedTodo_ThenThrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        final User mockOwner = mock(User.class);
        when(mockOwner.getUsername()).thenReturn("not me");
        final Todo mockTodo = mock(Todo.class);
        when(mockTodo.getOwner()).thenReturn(mockOwner);

        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.of(mockTodo));
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertThrows(ResponseStatusException.class, () -> todoService.updateTodo(1L, new TodoData("text todo")));
    }

    @Test
    public void whenUpdateOwnedTodo_ThenDoesntThrow() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        final User mockOwner = mock(User.class);
        when(mockOwner.getUsername()).thenReturn("me");
        final Todo mockTodo = mock(Todo.class);
        when(mockTodo.getOwner()).thenReturn(mockOwner);

        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.of(mockTodo));
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertDoesNotThrow(() -> todoService.updateTodo(1L, new TodoData("text todo")));
    }

    @Test
    public void whenUpdateTodoThatDoesntExist_ThenTrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.empty());
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertThrows(ResponseStatusException.class, () -> todoService.updateTodo(1L, new TodoData("text todo")));
    }

    @Test
    public void whenDeleteTodoThatDoesntExist_ThenTrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.empty());
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertThrows(ResponseStatusException.class, () -> todoService.deleteTodo(1L));
    }

    @Test
    public void whenDeleteNotOwnedTodo_ThenTrowsResponseStatusException() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        final User mockOwner = mock(User.class);
        when(mockOwner.getUsername()).thenReturn("not me");
        final Todo mockTodo = mock(Todo.class);
        when(mockTodo.getOwner()).thenReturn(mockOwner);

        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.of(mockTodo));
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);


        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertThrows(ResponseStatusException.class, () -> todoService.deleteTodo(1L));
    }


    @Test
    public void whenDeleteOwnedTodo_ThenDoesntThrow() {
        final UserPrincipal principal = new UserPrincipal("me");
        final UserRepository mockUserRepository = mock(UserRepository.class);
        final TodoRepository mockTodoRepository = mock(TodoRepository.class);
        final User mockOwner = mock(User.class);
        when(mockOwner.getUsername()).thenReturn("me");
        final Todo mockTodo = mock(Todo.class);
        when(mockTodo.getOwner()).thenReturn(mockOwner);

        when(mockTodoRepository.findById(anyLong())).thenReturn(Optional.of(mockTodo));
        final AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);
        when(mockAuthenticationService.getCurrentUser()).thenReturn(principal);

        final TodoServiceImpl todoService = new TodoServiceImpl(mockTodoRepository, mockUserRepository, mockAuthenticationService);
        assertDoesNotThrow(() -> todoService.deleteTodo(1L));
    }
}
