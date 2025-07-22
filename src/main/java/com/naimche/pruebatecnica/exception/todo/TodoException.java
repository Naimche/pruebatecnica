package com.naimche.pruebatecnica.exception.todo;

public class TodoException extends RuntimeException {
    private final String code;


    public TodoException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}