package com.naimche.pruebatecnica.exception;

public abstract class BaseApplicationException extends RuntimeException {
    private final String code;

    protected BaseApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}