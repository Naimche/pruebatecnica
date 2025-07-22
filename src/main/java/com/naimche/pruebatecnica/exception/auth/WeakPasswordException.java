package com.naimche.pruebatecnica.exception.auth;

public class WeakPasswordException extends AuthException {
    public WeakPasswordException() {
        super( "WEAK_PASSWORD","Password is too weak. It must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.");
    }

}
