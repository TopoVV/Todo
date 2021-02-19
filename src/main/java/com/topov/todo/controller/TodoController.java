package com.topov.todo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class TodoController {
    @GetMapping("/todos/{todoId}")
    public Map<String, Object> todoGet() {
        return Collections.emptyMap();
    }
    @GetMapping("/todos")
    public Map<String, Object> todoGetAll() {
        return Collections.emptyMap();
    }
    @PostMapping("/todos")
    public Map<String, Object> todoPost() {
        return Collections.emptyMap();
    }
    @PutMapping("/todos/{todoId}")
    public Map<String, Object> todoPut() {
        return Collections.emptyMap();
    }
    @DeleteMapping("/todos/{todoId}")
    public Map<String, Object> todoDelete() {
        return Collections.emptyMap();
    }
}
