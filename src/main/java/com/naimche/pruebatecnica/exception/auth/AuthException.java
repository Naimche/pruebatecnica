package com.naimche.pruebatecnica.exception.auth;

import com.naimche.pruebatecnica.exception.BaseApplicationException;

public class AuthException extends BaseApplicationException {

    public AuthException(String code, String message) {
        super(code, message);
    }


}