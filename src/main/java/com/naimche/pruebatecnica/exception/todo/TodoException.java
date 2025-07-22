package com.naimche.pruebatecnica.exception.todo;

import com.naimche.pruebatecnica.exception.BaseApplicationException;

public class TodoException extends BaseApplicationException {
    public TodoException(String code, String message) {
        super(code, message);
    }

}