package com.topov.todo.controller;

import com.topov.todo.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todos")
@SequenceGenerator(name = "todo_seq_gen", sequenceName = "todo_id_seq", allocationSize = 1)
public class Todo {
    @Id
    @GeneratedValue(generator = "todo_seq_gen", strategy = GenerationType.SEQUENCE)
    private Long todoId;
    private String text;
    private Boolean isDone;

    @ManyToOne(optional = false)
    private User owner;

    public Todo(String text, User owner) {
        this.text = text;
        this.isDone = false;
        this.owner = owner;
    }
}
