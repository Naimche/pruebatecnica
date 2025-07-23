package com.naimche.pruebatecnica.exception.auth;


public class NotAuthorizedException extends AuthException {
    public NotAuthorizedException() {
        super("NOT_AUTHORIZED", "Not authorized" );
    }
}
