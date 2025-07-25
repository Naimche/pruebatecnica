package com.naimche.pruebatecnica.dto;

public class AuthRequestDto {
    private String username;
    private String password;

    public AuthRequestDto() {}

    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}