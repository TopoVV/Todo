package com.topov.todo.repository;

import com.topov.todo.controller.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {
}
