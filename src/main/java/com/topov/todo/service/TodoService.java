package com.topov.todo.service;

import com.topov.todo.dto.TodoData;

public interface TodoService {
    boolean getTodo(Long id);
    boolean getAllTodos();
    boolean createTodo(TodoData createTodoData);
    boolean updateTodo(Long id, TodoData updateTodoData);
    boolean finishTodo(Long id);
    boolean deleteTodo(Long id);
}
