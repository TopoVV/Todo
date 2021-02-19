package com.topov.todo.service;

import com.topov.todo.controller.Todo;
import com.topov.todo.dto.TodoData;
import com.topov.todo.exception.AuthenticationException;
import com.topov.todo.model.User;
import com.topov.todo.repository.TodoRepository;
import com.topov.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
    private UserRepository userRepository;
    private TodoRepository todoRepository;
    private AuthenticationService authenticationService;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean getTodo(Long id) {
        return false;
    }

    @Override
    public boolean getAllTodos() {
        return false;
    }

    @Override
    @Transactional
    public boolean createTodo(TodoData createTodoData) {
        final Principal principal = this.authenticationService.getCurrentUser()
            .orElseThrow(() -> new AuthenticationException("Not authenticated"));

        final User user = this.userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new AuthenticationException("Not authenticated"));

        final Todo todo = new Todo(createTodoData.getText(), user);
        user.addTodo(todo);
        return true;
    }

    @Override
    public boolean updateTodo(Long id, TodoData updateTodoData) {
        final Principal principal = this.authenticationService.getCurrentUser()
            .orElseThrow(() -> new AuthenticationException("Not authenticated"));


    }

    @Override
    public boolean finishTodo(Long id) {
        return false;
    }

    @Override
    public boolean deleteTodo(Long id) {
        return false;
    }
}
