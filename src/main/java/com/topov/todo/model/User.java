package com.topov.todo.model;

import com.topov.todo.controller.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todo_users")
@SequenceGenerator(name = "user_seq_generator", sequenceName = "user_id_seq", allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(generator = "user_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Todo> todos;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addTodo(Todo todo) {
        todo.setOwner(this);
        this.todos.add(todo);
    }
}
