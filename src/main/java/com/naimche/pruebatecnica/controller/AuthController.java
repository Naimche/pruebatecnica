package com.naimche.pruebatecnica.controller;


import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = service.registerUser(createUserDto);
        return ResponseEntity.ok(createdUser);
    }
}
