package com.naimche.pruebatecnica.exception.user;

import com.naimche.pruebatecnica.exception.BaseApplicationException;

public class UserException extends BaseApplicationException {

    public UserException( String code, String message) {
        super(code, message);
    }


}
