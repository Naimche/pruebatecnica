package com.naimche.pruebatecnica.exception.todo;

public class TodoNotFound extends TodoException {
    public TodoNotFound() {
        super("TODO_NOT_FOUND", "Todo not found" );
    }
}
