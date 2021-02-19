package com.topov.todo.controller;

import com.topov.todo.model.Todo;
import com.topov.todo.dto.request.TodoData;
import com.topov.todo.dto.response.TodoDto;
import com.topov.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TodoController {
    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos/{todoId}")
    public Map<String, Object> todoGet(@PathVariable Long todoId) {
        final Todo todo = this.todoService.getTodo(todoId);
        final HashMap<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("value", new TodoDto(todo));
        return response;
    }

    @GetMapping("/todos")
    public Map<String, Object> todoGetAll() {
        final List<TodoDto> allTodos = this.todoService.getAllTodos()
            .stream()
            .map(TodoDto::new)
            .collect(Collectors.toList());

        final HashMap<String, Object> response = new HashMap<>();
        response.put("result", "success");
        response.put("count", allTodos.size());
        response.put("todos", allTodos);
        return response;
    }

    @PostMapping("/todos")
    public Map<String, Object> todoPost(@RequestBody @Valid TodoData createTodoData) {
        this.todoService.createTodo(createTodoData);
        return Collections.singletonMap("result", "success");
    }

    @PutMapping("/todos/{todoId}")
    public Map<String, Object> todoPut(@RequestBody @Valid TodoData updateTodoData, @PathVariable Long todoId) {
        this.todoService.updateTodo(todoId, updateTodoData);
        return Collections.singletonMap("result", "success");
    }

    @PutMapping("/todos/{todoId}/finish")
    public Map<String, Object> todoPutFinish(@PathVariable Long todoId) {
        this.todoService.finishTodo(todoId);
        return Collections.singletonMap("result", "success");
    }

    @DeleteMapping("/todos/{todoId}")
    public Map<String, Object> todoDelete(@PathVariable Long todoId) {
        this.todoService.deleteTodo(todoId);
        return Collections.singletonMap("result", "success");
    }
}
