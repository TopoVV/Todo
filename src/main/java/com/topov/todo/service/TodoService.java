package com.topov.todo.service;

import com.topov.todo.dto.Todo;
import com.topov.todo.dto.TodoData;

import java.util.List;

public interface TodoService {
    Todo getTodo(Long id);
    List<Todo> getAllTodos();
    void createTodo(TodoData createTodoData);
    void updateTodo(Long id, TodoData updateTodoData);
    void finishTodo(Long id);
    void deleteTodo(Long id);
}
