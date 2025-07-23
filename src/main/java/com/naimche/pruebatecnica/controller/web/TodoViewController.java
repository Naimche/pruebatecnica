package com.naimche.pruebatecnica.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoViewController {

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/")
    public String path() {
        return "login";
    }

    @GetMapping("/todos")
    public String todosView() {
        return "todos";
    }

    @GetMapping("/todos/create")
    public String createTodoView(@RequestParam(required = false) Long id) {
        return "create-todos";
    }

}