package com.kadiryuksel.peratestcase.response;

import org.springframework.http.HttpStatus;

public class NotFoundExceptionResponse extends Response{
    public NotFoundExceptionResponse(String message) {
        super(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, message);
    }
}
