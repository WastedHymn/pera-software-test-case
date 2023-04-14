package com.kadiryuksel.peratestcase.response;

import org.springframework.http.HttpStatus;

public class AlreadyExistsExceptionResponse extends Response{
    public AlreadyExistsExceptionResponse(String message) {
        super(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, message);
    }
}
