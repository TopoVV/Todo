package com.topov.todo.repository;

import com.topov.todo.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
}
