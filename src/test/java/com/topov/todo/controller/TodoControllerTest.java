package com.topov.todo.controller;

import com.topov.todo.converter.BindingResultConverter;
import com.topov.todo.dto.Todo;
import com.topov.todo.model.User;
import com.topov.todo.service.TodoService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class TodoControllerTest {

    @Test
    public void todoGetAll() throws Exception {
        final List<Todo> todos = Arrays.asList(
            new Todo(1L, "todo1", true, new User(1L, "user1", "password", Lists.emptyList())),
            new Todo(2L, "todo2", false, new User(2L, "user2", "password", Lists.emptyList())),
            new Todo(3L, "todo3", true, new User(3L, "user3", "password", Lists.emptyList())),
            new Todo(4L, "todo4", true, new User(1L, "user1", "password", Lists.emptyList())),
            new Todo(5L, "todo5", false, new User(2L, "user2", "password", Lists.emptyList()))
        );

        final TodoService mockTodoService = mock(TodoService.class);
        when(mockTodoService.getAllTodos()).thenReturn(todos);

        final MockMvc mvc = MockMvcBuilders.standaloneSetup(new TodoController(mockTodoService, new BindingResultConverter()))
            .build();

        mvc.perform(get("/todos"))
            .andDo(print());

    }

    @Test
    public void todoGet() throws Exception {
        final Todo todo = new Todo(1L, "todo1", true, new User(1L, "user1", "password", Lists.emptyList()));

        final TodoService mockTodoService = mock(TodoService.class);
        when(mockTodoService.getTodo(anyLong())).thenReturn(todo);

        final MockMvc mvc = MockMvcBuilders.standaloneSetup(new TodoController(mockTodoService, new BindingResultConverter()))
            .build();

        mvc.perform(get("/todos/1"))
            .andDo(print());
    }
}
