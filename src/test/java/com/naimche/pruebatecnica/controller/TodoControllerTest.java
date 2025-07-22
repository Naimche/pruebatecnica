package com.naimche.pruebatecnica.controller;

import com.naimche.pruebatecnica.dto.TodoDto;
import com.naimche.pruebatecnica.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoControllerTest {

    @Test
    void findAll_returnsPaged() {
        TodoService todoService = Mockito.mock(TodoService.class);
        TodoController controller = new TodoController(todoService);

        TodoDto todo1 = new TodoDto(1L, "Tarea 1", false, 1L, "usuario1", "España");
        TodoDto todo2 = new TodoDto(2L, "Tarea 2", true, 1L, "usuario1","Argentina");
        List<TodoDto> todos = List.of(todo1, todo2);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<TodoDto> page = new PageImpl<>(todos, pageable, todos.size());

        Mockito.when(todoService.findAllTodo(pageable)).thenReturn(page);

        ResponseEntity<Page<TodoDto>> response = controller.findAll(0, 2, "id", "asc", "null", null);

        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Tarea 1", response.getBody().getContent().get(0).getTitle());
        assertEquals("Tarea 2", response.getBody().getContent().get(1).getTitle());
    }
}
