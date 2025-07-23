package com.naimche.pruebatecnica.service;


import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.exception.auth.NotAuthorizedException;
import com.naimche.pruebatecnica.exception.todo.TodoNotFound;
import com.naimche.pruebatecnica.exception.user.UserNotFound;
import com.naimche.pruebatecnica.mapper.TodoMapper;
import com.naimche.pruebatecnica.entity.Todo;
import com.naimche.pruebatecnica.entity.User;
import com.naimche.pruebatecnica.repository.TodoRepository;
import com.naimche.pruebatecnica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class TodoService {

    private static final Pattern MALICIOUS_PATTERN = Pattern.compile(
            "[<>\"'%;()&+\\-/*=]|script|javascript|vbscript|onload|onerror",
            Pattern.CASE_INSENSITIVE
    );

    @Value("${max.title.length:100}")
    private int maxTitleLength;
    @Value("${max.username.length:50}")
    private int maxUsernameLength;
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
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User authenticatedUser = userRepository.findByUsername(userId)
                .orElseThrow(UserNotFound::new);

        User user = userRepository.findById(todoDto.getUserId())
                .orElseThrow(UserNotFound::new);

        if (!Objects.equals(authenticatedUser.getId(), user.getId())) {
            throw new NotAuthorizedException();
        }

        Todo todo = TodoMapper.INSTANCE.toEntity(todoDto);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);
        return TodoMapper.INSTANCE.toDto(savedTodo);
    }

    public TodoDto saveWithoutAuth(TodoDto todoDto) {
        User user = userRepository.findById(todoDto.getUserId())
                .orElseThrow(UserNotFound::new);

        Todo todo = TodoMapper.INSTANCE.toEntity(todoDto);
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);
        return TodoMapper.INSTANCE.toDto(savedTodo);
    }

    public Page<TodoDto> findAllTodoFiltered(Pageable pageable, String title, String username) {

        // Validate the title/username parameters to prevent XSS attacks.
        // I implement the validation here instead of in another class because no other queries require character escaping.
        // For security reasons, no explicit error codes or detailed messages are returned to avoid revealing validation logic and make it harder to detect.
        if (title != null && (MALICIOUS_PATTERN.matcher(title).find() || title.length() > maxTitleLength)) {
            throw new IllegalArgumentException("Invalid characters");
        }

        if (username != null && (MALICIOUS_PATTERN.matcher(username).find() || username.length() > maxUsernameLength)) {
            throw new IllegalArgumentException("Invalid characters");
        }

        Page<Todo> todos;

        todos = todoRepository.findAllFiltered(title, username, pageable);

        return todos.map(TodoMapper.INSTANCE::toDto);
    }

    public TodoDto findById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.map(TodoMapper.INSTANCE::toDto).orElseThrow(TodoNotFound::new);
    }


    public boolean delete(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(TodoNotFound::new);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User authenticatedUser = userRepository.findByUsername(userId)
                .orElseThrow(UserNotFound::new);

        if (!Objects.equals(authenticatedUser.getId(), todo.getUser().getId())) {
            throw new NotAuthorizedException();
        }

        todoRepository.delete(todo);
        return true;
    }
}
