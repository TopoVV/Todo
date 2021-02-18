package com.topov.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
