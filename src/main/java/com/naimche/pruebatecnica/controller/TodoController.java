package com.naimche.pruebatecnica.controller;

import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TodoDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String username) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<TodoDto> todos = service.findAllTodoFiltered(pageable, title, username);

        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody TodoDto todoDto) {
        return ResponseEntity.ok(service.saveWithoutAuth(todoDto));
    }

    @PatchMapping
    @RequestMapping("/update/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable Long id) {
        if (todoDto == null || id == null) {
            return ResponseEntity.badRequest().build();
        }


        todoDto.setId(id);
        TodoDto updatedTodo = service.save(todoDto);
        return ResponseEntity.ok(updatedTodo);
    }

    @GetMapping
    @RequestMapping("/id/{id}")
    public ResponseEntity<TodoDto> findById(@PathVariable Long id) {
        TodoDto todo = service.findById(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
