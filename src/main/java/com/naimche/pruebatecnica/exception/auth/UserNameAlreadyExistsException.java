package com.naimche.pruebatecnica.exception.auth;

public class UserNameAlreadyExistsException extends AuthException {

    public UserNameAlreadyExistsException() {
        super("USER_NAME_ALREADY_EXISTS", "The username already exists");

    }

}
