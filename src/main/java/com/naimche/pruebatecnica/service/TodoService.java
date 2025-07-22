package com.naimche.pruebatecnica.service;


import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.exception.todo.TodoNotFound;
import com.naimche.pruebatecnica.exception.user.UserNotFound;
import com.naimche.pruebatecnica.mapper.TodoMapper;
import com.naimche.pruebatecnica.model.Todo;
import com.naimche.pruebatecnica.model.User;
import com.naimche.pruebatecnica.repository.TodoRepository;
import com.naimche.pruebatecnica.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Page<TodoDto> findAllTodo(Pageable pageable) {
        return todoRepository.findAll(pageable).map(TodoMapper.INSTANCE::toDto);
    }

    public TodoDto save(TodoDto todoDto) {

        User user = userRepository.findById(todoDto.getUserId())
                .orElseThrow(UserNotFound::new);

        Todo todo = TodoMapper.INSTANCE.toEntity(todoDto);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);
        return TodoMapper.INSTANCE.toDto(savedTodo);
    }

    public Page<TodoDto> findAllTodoFiltered(Pageable pageable, String title, String username) {
        Page<Todo> todos;

        todos = todoRepository.findAllFiltered(title, username, pageable);

        return todos.map(TodoMapper.INSTANCE::toDto);
    }

    public TodoDto findById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.map(TodoMapper.INSTANCE::toDto).orElseThrow(TodoNotFound::new);
    }


}
