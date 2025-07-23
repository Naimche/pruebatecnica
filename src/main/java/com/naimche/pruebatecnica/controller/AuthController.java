package com.naimche.pruebatecnica.controller;


import com.naimche.pruebatecnica.dto.AuthRequestDto;
import com.naimche.pruebatecnica.dto.CreateUserDto;
import com.naimche.pruebatecnica.dto.UserDto;
import com.naimche.pruebatecnica.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${security.httpsEnabled}")
    private boolean httpsEnabled;


    private final AuthService service;
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = service.registerUser(createUserDto);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticate(@RequestBody AuthRequestDto request, HttpServletResponse response) {

        String authResponse = service.authenticate(request);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", authResponse)
                .httpOnly(true)
                .secure(httpsEnabled)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 día
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok().build();
    }
}
