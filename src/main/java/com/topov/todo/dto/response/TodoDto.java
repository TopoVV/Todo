package com.topov.todo.dto.response;

import com.topov.todo.model.Todo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDto {
    private Long id;
    private String text;
    private String status;

    public TodoDto(Todo todo) {
        this.id = todo.getTodoId();
        this.text = todo.getText();
        this.status = todo.getIsDone() ? "done" : "not done";
    }
}
