package com.naimche.pruebatecnica.exception.user;

public class UserException extends RuntimeException {
    private final String code;

    public UserException( String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
