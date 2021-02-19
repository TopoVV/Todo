package com.topov.todo.service;

import com.topov.todo.model.Todo;
import com.topov.todo.dto.request.TodoData;
import com.topov.todo.model.User;
import com.topov.todo.repository.TodoRepository;
import com.topov.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.logging.LogManager;

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
    @Transactional
    public Todo getTodo(Long id) {
        final Principal principal =  this.authenticationService.getCurrentUser();

        final Todo todo = this.todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!todo.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return todo;
    }

    @Override
    @Transactional
    public List<Todo> getAllTodos() {
        final Principal principal = this.authenticationService.getCurrentUser();
        final User user = this.userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return user.getTodos();
    }

    @Override
    @Transactional
    public void createTodo(TodoData createTodoData) {
        final Principal principal = this.authenticationService.getCurrentUser();

        final User user = this.userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        final Todo todo = new Todo(createTodoData.getText(), user);
        user.addTodo(todo);
    }

    @Override
    @Transactional
    public void updateTodo(Long id, TodoData updateTodoData) {
        final Principal principal =  this.authenticationService.getCurrentUser();

        final Todo todo = this.todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!todo.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        todo.setText(updateTodoData.getText());
    }

    @Override
    @Transactional
    public void finishTodo(Long id) {
        final Principal principal = this.authenticationService.getCurrentUser();

        final Todo todo = this.todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!todo.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        todo.setIsDone(true);
    }

    @Override
    @Transactional
    public void deleteTodo(Long id) {
        final Principal principal = this.authenticationService.getCurrentUser();

        final Todo todo = this.todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!todo.getOwner().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        this.todoRepository.delete(todo);
    }
}
