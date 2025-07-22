package com.naimche.pruebatecnica.exception.user;

public class UserNotFound extends UserException {
    public UserNotFound() {
        super("USER_NOT_FOUND", "User not found");
    }
}
